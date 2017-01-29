package com.frascu.bot.newsbot.schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.frascu.bot.newsbot.dao.NewsDao;
import com.frascu.bot.newsbot.dto.NewsDto;
import com.frascu.bot.newsbot.rss.FeedMessage;
import com.frascu.bot.newsbot.rss.RSSFeedParser;
import com.frascu.bot.newsbot.telegram.BotConfig;
import com.frascu.bot.newsbot.telegram.handler.NewsHandler;

public class NewsJob implements Job {

	private static final int MIN_OF_N_WORDS_IN_TITLE = 5;

	private static final int LENGHT_OF_WORD_TO_CHECK = 5;

	private static final Logger LOGGER = Logger.getLogger(NewsJob.class);

	private NewsDao newsDao = NewsDao.getInstance();

	private static boolean isFirstRun = true;

	public NewsJob() {
		super();
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {

			// Read RSS
			RSSFeedParser parser = new RSSFeedParser(BotConfig.getSources());

			LOGGER.info("Reading the rss...");
			List<FeedMessage> feed = parser.readFeed();

			List<NewsDto> newsDtos = newsDao.saveNewsFromFeeds(feed);

			List<NewsDto> newsToSend = new ArrayList<>();
			List<NewsDto> newsSimilar = new ArrayList<>();

			for (NewsDto newsDto : newsDtos) {
				String title = newsDto.getTitle().replaceAll(",", "");
				String[] words = title.split(" ");
				long numberOfWordsInOtherNewsToday = Arrays.asList(words).stream().filter(word -> {
					String cleanWord = word.trim();
					return cleanWord.length() > LENGHT_OF_WORD_TO_CHECK
							&& newsDao.areOtherNewsWithWordToday(cleanWord, newsDto.getId());
				}).count();
				if (numberOfWordsInOtherNewsToday > MIN_OF_N_WORDS_IN_TITLE) {
					newsSimilar.add(newsDto);
				} else {
					newsToSend.add(newsDto);
				}
			}

			if (!isFirstRun) {
				NewsHandler newsHandler = new NewsHandler();
				// Send the news
				newsHandler.sendNewsToAllUsers(newsToSend);
				newsDao.setNewsAsSent(newsToSend);

				// Send the similar news to the admin
				newsHandler.sendNewsToAdmin(newsSimilar);
				
			} else {
				setFirstRun(false);
			}

		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	public static void setFirstRun(boolean isFirstRun) {
		NewsJob.isFirstRun = isFirstRun;
	}

}

package com.frascu.bot.newsbot.schedule;

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

	private static final Logger LOGGER = Logger.getLogger(NewsJob.class);

	private NewsDao newsDao = NewsDao.getInstance();

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

			new NewsHandler().sendMessageToAllUsers(newsDtos);
			newsDao.setNewsAsSent(newsDtos);

		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

}

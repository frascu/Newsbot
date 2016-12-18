package com.frascu.bot.newsbot.schedule;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.dao.UserDao;
import com.frascu.bot.newsbot.rss.FeedMessage;
import com.frascu.bot.newsbot.rss.RSSFeedParser;
import com.frascu.bot.newsbot.telegram.handler.NewsHandler;

public class NewsJob implements Job {

	private static final Logger LOGGER = Logger.getLogger(NewsJob.class);

	private UserDao userDao = UserDao.getInstance();

	public NewsJob() {
		super();
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			// Sources
			List<String> urls = Arrays.asList("http://www.comune.sannicandro.bari.it/index.php?format=feed&type=rss",
					"https://news.google.com/news/section?q=sannicandro%20di%20bari&output=rss");

			// Read RSS
			RSSFeedParser parser = new RSSFeedParser(urls);

			final Date day = new Date();
			LOGGER.info("Day: " + day);

			LOGGER.info("Reading the rss...");
			List<FeedMessage> feed = parser.readFeed();

			LOGGER.info("Getting the last message to send...");
			Optional<FeedMessage> optional = feed.stream().filter(n -> day.before(n.getPubDate())).findFirst();
			// Optional<FeedMessage> optional = feed.stream().findFirst();

			Collections.sort(feed, (FeedMessage f1, FeedMessage f2) -> f1.compareTo(f1));

			feed.forEach(n -> System.out.println(n.getPubDate()));

			if (optional.isPresent()) {
				sendMessageToAllUsers(optional.get(), new NewsHandler());
			} else {
				LOGGER.info("There are no messages to send.");
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	private void sendMessageToAllUsers(FeedMessage lastMessageToSend, NewsHandler bot) {

		List<Long> userIds = userDao.getUserIdsRegistered();
		for (Long userId : userIds) {
			// Send Message
			LOGGER.debug("Sending the message...");
			SendMessage message = new SendMessage();
			message.setChatId(userId.toString());
			message.setText(lastMessageToSend.getLink());
			try {
				bot.sendMessage(message);
				LOGGER.info("Message sent.");
			} catch (TelegramApiException e) {
				LOGGER.error("Impossible to send the message.", e);
			}
		}

	}

}

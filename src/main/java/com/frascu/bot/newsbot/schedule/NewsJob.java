package com.frascu.bot.newsbot.schedule;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
	private static Date lastDateChecked = new Date();

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
//			Collections.sort(feed, (f1, f2) -> f1.compareTo(f2));
//			for (FeedMessage feedMessage : feed) {
//				System.out.println(feedMessage.getPubDate());
//			}

			LOGGER.info("Finding news between " + lastDateChecked + " and " + day);
			List<FeedMessage> filteredFeed = feed.stream()
					.filter(n -> n.getPubDate().after(lastDateChecked) && n.getPubDate().before(day))
					.collect(Collectors.toList());

			if (filteredFeed != null && !filteredFeed.isEmpty()) {
				for (FeedMessage feedMessage : filteredFeed) {
					sendMessageToAllUsers(feedMessage, new NewsHandler());
				}
			} else {
				LOGGER.info("There are no messages to send.");
			}

			// Update the last date checked
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(day);
			calendar.add(Calendar.SECOND, 1);
			lastDateChecked = calendar.getTime();
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
			if (lastMessageToSend.getLink() != null && !lastMessageToSend.getLink().isEmpty()){
				message.setText(lastMessageToSend.getLink());
			}else{
				message.setText(lastMessageToSend.getGuid());
			}
			try {
				bot.sendMessage(message);
				LOGGER.info("Message sent.");
			} catch (TelegramApiException e) {
				LOGGER.error("Impossible to send the message.", e);
			}
		}

	}

}

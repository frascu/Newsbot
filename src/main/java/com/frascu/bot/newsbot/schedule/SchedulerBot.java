package com.frascu.bot.newsbot.schedule;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.rss.FeedMessage;
import com.frascu.bot.newsbot.rss.RSSFeedParser;
import com.frascu.bot.newsbot.telegram.NewsBot;

public class SchedulerBot {

	private static final Logger LOGGER = Logger.getLogger(SchedulerBot.class);

	public SchedulerBot() {
		super();
	}

	public static void main(String[] args) throws ParseException {
		SchedulerBot schedulerBot = new SchedulerBot();
		schedulerBot.execute();
	}

	public void execute() {
		// Read RSS
		RSSFeedParser parser = new RSSFeedParser(
				"http://www.comune.sannicandro.bari.it/index.php?format=feed&type=rss");

		final Date day = new Date();
		LOGGER.info("Day: " + day);

		LOGGER.info("Reading the rss...");
		List<FeedMessage> feed = parser.readFeed();

		LOGGER.info("Getting the last message to send...");
		Optional<FeedMessage> optional = feed.stream().filter(n -> day.before(n.getPubDate())).findFirst();

		if (optional.isPresent()) {
			FeedMessage lastMessageToSend = optional.get();

			// Create bot
			NewsBot bot = new NewsBot();

			// Send Message
			LOGGER.info("Sending the message...");
			SendMessage message = new SendMessage();
			message.setChatId(bot.getAdmin());
			message.setText(lastMessageToSend.getLink());

			try {
				bot.sendMessage(message);
				LOGGER.info("Message sent.");
			} catch (TelegramApiException e) {
				LOGGER.error("Impossible to send the message.", e);
			}
		} else {
			LOGGER.info("Today There are no messages to send.");
		}
	}

}

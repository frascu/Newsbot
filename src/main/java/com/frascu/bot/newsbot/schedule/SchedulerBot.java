package com.frascu.bot.newsbot.schedule;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.rss.FeedMessage;
import com.frascu.bot.newsbot.rss.RSSFeedParser;
import com.frascu.bot.newsbot.telegram.NewsBot;

public class SchedulerBot {

	private static final Logger LOGGER = Logger.getLogger(SchedulerBot.class.getName());

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

		// Set the day to analyze
		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		// final Date day = sdf.parse("11/10/2016");

		final Date day = new Date();
		LOGGER.log(Level.INFO, "Day: {0}", day);

		LOGGER.log(Level.INFO, "Reading the rss...");
		List<FeedMessage> feed = parser.readFeed();

		LOGGER.log(Level.INFO, "Getting the last message to send...");
		Optional<FeedMessage> optional = feed.stream().filter(n -> day.before(n.getPubDate())).findFirst();

		if (optional.isPresent()) {
			FeedMessage lastMessageToSend = optional.get();

			// Create bot
			NewsBot bot = new NewsBot();

			// Send Message
			LOGGER.log(Level.INFO, "Sending the message...");
			SendMessage message = new SendMessage();
			message.setChatId(bot.getAdmin());
			message.setText(lastMessageToSend.getLink());

			try {
				bot.sendMessage(message);
				LOGGER.log(Level.INFO, "Message sent.");
			} catch (TelegramApiException e) {
				LOGGER.log(Level.SEVERE, "Impossible to send the message.", e);
			}
		} else {
			LOGGER.log(Level.INFO, "Today There are no messages to send.");
		}
	}

}

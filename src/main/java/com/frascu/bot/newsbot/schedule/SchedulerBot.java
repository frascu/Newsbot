package com.frascu.bot.newsbot.schedule;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.rss.FeedMessage;
import com.frascu.bot.newsbot.rss.RSSFeedParser;
import com.frascu.bot.newsbot.telegram.NewsBot;

public class SchedulerBot {

	public static void main(String[] args) throws ParseException {

		// Read RSS
		RSSFeedParser parser = new RSSFeedParser("http://www.comune.sannicandro.bari.it/index.php?format=feed&type=rss");

		// Set the day to analyze
		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		// final Date day = sdf.parse("11/10/2016");

		final Date day = new Date();
		System.out.println("Day: " + day);

		System.out.println("Reading the rss...");
		List<FeedMessage> feed = parser.readFeed();

		try {
			System.out.println("Getting the last message to send...");
			FeedMessage lastMessageToSend = feed.stream().filter(n -> n.getPubDate().after(day)).findFirst().get();

			// Create bot
			NewsBot bot = new NewsBot();

			// Send Message
			System.out.println("Sending the message...");
			SendMessage message = new SendMessage();
			message.setChatId(bot.getAdmin());
			message.setText(lastMessageToSend.getLink());

			try {
				bot.sendMessage(message);
				System.out.println("Message sent.");
			} catch (TelegramApiException e) {
				System.err.println("Impossible to send the message.");
			}
		} catch (NoSuchElementException e) {
			System.out.println("Today There are no messages to send.");
		}
	}

}

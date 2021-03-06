package com.frascu.bot.newsbot.telegram.handler;

import java.util.List;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.dao.GroupDao;
import com.frascu.bot.newsbot.dao.UserDao;
import com.frascu.bot.newsbot.dto.NewsDto;
import com.frascu.bot.newsbot.telegram.BotConfig;

public class NewsHandler extends TelegramLongPollingBot {

	private static final Logger LOGGER = Logger.getLogger(NewsHandler.class);
	private UserDao userDao = new UserDao();
	private GroupDao groupDao = new GroupDao();

	public NewsHandler() {
		super();
	}

	@Override
	public String getBotUsername() {
		return BotConfig.NAME;
	}

	@Override
	public void onUpdateReceived(Update arg0) {
		// NOTHING TO IMPLEMENT
	}

	@Override
	public String getBotToken() {
		return BotConfig.TOKEN;
	}

	public Long getAdmin() {
		return BotConfig.ADMIN;
	}

	public void sendNewsToAdmin(List<NewsDto> newsList) {
		if (newsList != null && !newsList.isEmpty()) {
			for (NewsDto news : newsList) {
				// Send Message
				LOGGER.debug("Sending the message...");
				SendMessage message = new SendMessage();
				message.enableHtml(true);
				message.setChatId(String.valueOf(BotConfig.ADMIN));
				message.setText(new StringBuilder("Ho deciso di non inviare questa notizia perchè dovrei averne inviata una simile oggi:\n<b>").append(news.getTitle())
						.append("</b>\n").append(news.getLink()).toString());
				try {
					sendMessage(message);
					LOGGER.info("Message sent.");
				} catch (TelegramApiException e) {
					LOGGER.error("Impossible to send the message.", e);
				}
			}
		} else {
			LOGGER.info("There are no messages to send to the admin.");
		}
	}

	public void sendNewsToAllUsers(List<NewsDto> newsToSend) {
		if (newsToSend != null && !newsToSend.isEmpty()) {
			List<Long> userIds = userDao.getUserIdsRegistered();
			List<Long> groupIds = groupDao.getGroupIdsRegistered();
			for (NewsDto news : newsToSend) {
				userIds.forEach(u -> sendNewsToChat(news, u));
				groupIds.forEach(g -> sendNewsToChat(news, g));
			}

		} else {
			LOGGER.info("There are no messages to send.");
		}
	}

	private void sendNewsToChat(NewsDto news, Long chatId) {
		// Send Message
		LOGGER.debug("Sending the message...");
		SendMessage message = new SendMessage();
		message.enableHtml(true);
		message.setChatId(String.valueOf(chatId));
		message.setText(
				new StringBuilder("<b>").append(news.getTitle()).append("</b>\n").append(news.getLink()).toString());
		try {
			sendMessage(message);
			LOGGER.info("Message sent.");
		} catch (TelegramApiException e) {
			LOGGER.error("Impossible to send the message.", e);
		}
	}
}

package com.frascu.bot.newsbot.telegram.handler;

import java.util.List;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.dao.UserDao;
import com.frascu.bot.newsbot.dto.NewsDto;
import com.frascu.bot.newsbot.telegram.BotConfig;

public class NewsHandler extends TelegramLongPollingBot {

	private static final Logger LOGGER = Logger.getLogger(NewsHandler.class);
	private UserDao userDao = UserDao.getInstance();

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

	public String getAdmin() {
		return BotConfig.ADMIN;
	}

	public void sendMessageToAllUsers(List<NewsDto> newsToSend) {
		if (newsToSend != null && !newsToSend.isEmpty()) {
			List<Long> userIds = userDao.getUserIdsRegistered();
			for (NewsDto news : newsToSend) {
				for (Long userId : userIds) {
					// Send Message
					LOGGER.debug("Sending the message...");
					SendMessage message = new SendMessage();
					message.setChatId(userId.toString());
					message.setText(news.getLink());
					try {
						sendMessage(message);
						LOGGER.info("Message sent.");
					} catch (TelegramApiException e) {
						LOGGER.error("Impossible to send the message.", e);
					}
				}
			}
		} else {
			LOGGER.info("There are no messages to send.");
		}

	}
}

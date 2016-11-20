package com.frascu.bot.newsbot.telegram.handler;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import com.frascu.bot.newsbot.telegram.BotConfig;

public class NewsHandler extends TelegramLongPollingBot {

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
}

package com.frascu.bot.newsbot.telegram;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class NewsBot extends TelegramLongPollingBot {

	private String token;
	private String admin;
	private static final String BOT_NAME = "NewsBot";

	public NewsBot() {
		super();
		
		// load a properties file
		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// get the property values
		this.token = prop.getProperty("token");
		this.admin = prop.getProperty("admin");
	}

	public String getBotUsername() {
		return BOT_NAME;
	}

	public void onUpdateReceived(Update arg0) {
	}

	@Override
	public String getBotToken() {
		return token;
	}

	public String getAdmin() {
		return admin;
	}
}

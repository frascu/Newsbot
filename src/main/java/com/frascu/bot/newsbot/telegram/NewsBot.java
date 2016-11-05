package com.frascu.bot.newsbot.telegram;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class NewsBot extends TelegramLongPollingBot {

	private static final Logger LOGGER = Logger.getLogger(NewsBot.class.getName());

	private String token;
	private String admin;
	private String name;

	public NewsBot() {
		super();

		// load a properties file
		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		LOGGER.log(Level.FINE, "Reading parameters from configuration file.");
		this.token = prop.getProperty("token");
		this.admin = prop.getProperty("admin");
		this.name = prop.getProperty("admin");
	}

	@Override
	public String getBotUsername() {
		return name;
	}

	@Override
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

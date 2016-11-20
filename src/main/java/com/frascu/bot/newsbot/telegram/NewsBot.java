package com.frascu.bot.newsbot.telegram;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class NewsBot extends TelegramLongPollingBot {

	private static final Logger LOGGER = Logger.getLogger(NewsBot.class);

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
			LOGGER.error("Reading parameters from configuration file.", e);
		}

		LOGGER.info("Reading parameters from configuration file.");
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
		//TO COMPLETE
	}

	@Override
	public String getBotToken() {
		return token;
	}

	public String getAdmin() {
		return admin;
	}
}

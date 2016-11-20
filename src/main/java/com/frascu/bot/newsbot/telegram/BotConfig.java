package com.frascu.bot.newsbot.telegram;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class BotConfig {

	private static final Logger LOGGER = Logger.getLogger(BotConfig.class);

	private static Properties prop;

	static { // load a properties file
		prop = new Properties();
		try {
			prop.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			LOGGER.error("Reading parameters from configuration file.", e);
		}
	}

	public static final String TOKEN = prop.getProperty("token");
	public static final String ADMIN = prop.getProperty("admin");
	public static final String NAME = prop.getProperty("name");

	private BotConfig() {
		super();
	}
}

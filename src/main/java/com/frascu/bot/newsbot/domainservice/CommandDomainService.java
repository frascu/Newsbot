package com.frascu.bot.newsbot.domainservice;

import java.util.List;

import org.telegram.telegrambots.api.objects.User;

import com.frascu.bot.newsbot.dao.UserDao;

public class CommandDomainService {

	private CommandDomainService() {
		super();
	}

	public static String start(User user) {
		UserDao userDao = new UserDao();
		StringBuilder messageBuilder = new StringBuilder();
		String userName = user.getFirstName() + " " + user.getLastName();

		if (userDao.isRegistered(user.getId())) {
			messageBuilder.append("Ciao ").append(userName).append("\n");
			messageBuilder.append("sei già iscritto.");
		} else {
			userDao.registerUser(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName());
			messageBuilder.append("Benvenuto ").append(userName).append("\n");
			messageBuilder.append("questo bot ti aggiornera' sulle ultime notizie di Sannicandro di Bari.");
		}
		return messageBuilder.toString();
	}

	public static String stop(User user) {
		UserDao userDao = new UserDao();
		StringBuilder messageBuilder = new StringBuilder();

		String userName = user.getFirstName() + " " + user.getLastName();

		if (!userDao.isRegistered(user.getId())) {
			messageBuilder.append("Ciao ").append(userName).append("\n");
			messageBuilder.append("hai gia' cancellato l'scrizione.");
		} else {
			userDao.unRegisterUser(user.getId());
			messageBuilder.append("Ciao ").append(userName).append("\n");
			messageBuilder.append("ci mancherai.");
		}
		return messageBuilder.toString();
	}

	public static String help(List<String> commands) {

		StringBuilder helpMessageBuilder = new StringBuilder("<b>Aiuto</b>\n");
		helpMessageBuilder.append("I comandi disponibili sono i seguenti:\n\n");

		for (String command : commands) {
			helpMessageBuilder.append(command).append("\n\n");
		}
		return null;
	}
}

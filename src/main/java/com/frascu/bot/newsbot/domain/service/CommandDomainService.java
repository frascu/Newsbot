package com.frascu.bot.newsbot.domain.service;

import java.util.List;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;

import com.frascu.bot.newsbot.dao.GroupDao;
import com.frascu.bot.newsbot.dao.UserDao;
import com.frascu.bot.newsbot.dto.CommandDto;
import com.frascu.bot.newsbot.dto.UserDto;
import com.frascu.bot.newsbot.telegram.Emoji;

public class CommandDomainService {
	private static CommandDomainService instance = new CommandDomainService();

	private static final String MESSAGE_START_ALREARY_REGISTERED = "Ciao %s\nsei già iscritto.";
	private static final String MESSAGE_START_WELCOME = "Benvenuto %S\nquesto bot Vi aggiornerà sulle ultime notizie di Sannicandro di Bari.";
	private static final String MESSAGE_STOP_ALREARY_NOT_REGISTERED = "Ciao %s\nhai già cancellato l'iscrizione.";
	private static final String MESSAGE_STOP_BYE = "Ciao %s\nci mancHerai.";

	private static final String MESSAGE_GROUP_START_ALREARY_REGISTERED = "Ciao %s\nsiete già iscritti alle notizie.";
	private static final String MESSAGE_GROUP_START_WELCOME = "Benvenuto %S\nquesto bot ti aggiornerà sulle ultime notizie di Sannicandro di Bari.";
	private static final String MESSAGE_GROUP_STOP_ALREARY_NOT_REGISTERED = "Ciao,\navete già cancellato l'iscrizione.";
	private static final String MESSAGE_GROUP_STOP_BYE = "Ciao\nci mancherete.";

	private CommandDomainService() {
		super();
	}

	public static CommandDomainService getInstance() {
		return instance;
	}

	public String startUser(User user) {
		UserDao userDao = new UserDao();
		String message = null;
		if (userDao.isRegistered(user.getId())) {
			message = MESSAGE_START_ALREARY_REGISTERED;
		} else {
			userDao.registerUser(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName());
			message = MESSAGE_START_WELCOME;
		}
		return String.format(message, user.getFirstName());
	}

	public String stopUser(User user) {
		UserDao userDao = new UserDao();
		String message = null;
		if (!userDao.isRegistered(user.getId())) {
			message = MESSAGE_STOP_ALREARY_NOT_REGISTERED;
		} else {
			userDao.unRegisterUser(user.getId());
			message = MESSAGE_STOP_BYE;
		}
		return String.format(message, user.getFirstName());
	}

	public String help(List<CommandDto> commands, boolean isAdmin) {

		StringBuilder helpMessageBuilder = new StringBuilder("<b>Aiuto</b>\n");
		helpMessageBuilder.append("I comandi disponibili sono i seguenti:\n\n");

		commands.stream().filter(command -> (!"admin".equals(command.getCommandIdentifier()) && !isAdmin) || isAdmin)
				.forEach(command -> helpMessageBuilder.append(command).append("\n\n"));

		return helpMessageBuilder.toString();
	}

	public String getUsers(String[] strings) {
		UserDao userDao = new UserDao();
		return getMessageWithAllUsers(userDao.getAllUsers());
	}

	private String getMessageWithAllUsers(List<UserDto> users) {
		StringBuilder messageBuilder = new StringBuilder();
		if (!users.isEmpty()) {
			messageBuilder.append("<b>Utenti:</b>\n");
			for (UserDto user : users) {
				messageBuilder//
						.append(user.isRegistered() ? Emoji.VICTORY_HAND : Emoji.CRYING_FACE).append(" - ")//
						.append(user.getUserId()).append(" - ")//
						.append(user.getFirstName()).append(" ")//
						.append("\n");
			}
		}
		return messageBuilder.toString();
	}

	public String startGroup(Chat chat) {
		GroupDao groupDao = new GroupDao();
		String message = null;
		if (groupDao.isRegistered(chat.getId())) {
			message = MESSAGE_GROUP_START_ALREARY_REGISTERED;
		} else {
			groupDao.registerGroup(chat.getId());
			message = MESSAGE_GROUP_START_WELCOME;
		}
		return String.format(message, chat.getTitle());

	}

	public String stopGroup(Chat chat) {
		GroupDao groupDao = new GroupDao();
		String message = null;
		if (!groupDao.isRegistered(chat.getId())) {
			message = MESSAGE_GROUP_STOP_ALREARY_NOT_REGISTERED;
		} else {
			groupDao.unRegisterGroup(chat.getId());
			message = MESSAGE_GROUP_STOP_BYE;
		}
		return message;
	}

}

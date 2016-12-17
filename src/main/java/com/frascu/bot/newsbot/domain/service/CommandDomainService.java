package com.frascu.bot.newsbot.domain.service;

import java.util.List;

import org.telegram.telegrambots.api.objects.User;

import com.frascu.bot.newsbot.dao.UserDao;
import com.frascu.bot.newsbot.dto.CommandDto;
import com.frascu.bot.newsbot.dto.UserDto;

public class CommandDomainService {

	private static CommandDomainService instance = new CommandDomainService();
	private UserDao userDao = UserDao.getInstance();

	private static final String COMMAND_NOT_EXISTING = "Il comando selezionato non esiste";

	private CommandDomainService() {
		super();
	}

	public static CommandDomainService getInstance() {
		return instance;
	}

	public String start(User user) {
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

	public String stop(User user) {
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

	public String help(List<CommandDto> commands, boolean isAdmin) {

		StringBuilder helpMessageBuilder = new StringBuilder("<b>Aiuto</b>\n");
		helpMessageBuilder.append("I comandi disponibili sono i seguenti:\n\n");

		commands.stream().filter(command -> (!"admin".equals(command.getCommandIdentifier()) && !isAdmin) || isAdmin)
				.forEach(command -> helpMessageBuilder.append(command).append("\n\n"));

		return helpMessageBuilder.toString();
	}

	public String admin(String[] strings) {

		StringBuilder messageBuilder = new StringBuilder();

		if (strings != null && strings.length > 0) {

			if (strings[0].equals("users")) {
				getMessageWithAllUsers(messageBuilder, userDao.getAllUsers());
			} else {
				messageBuilder.append(COMMAND_NOT_EXISTING);
			}
		} else {
			messageBuilder.append(COMMAND_NOT_EXISTING);
		}
		return messageBuilder.toString();
	}

	private void getMessageWithAllUsers(StringBuilder messageBuilder, List<UserDto> users) {
		if (!users.isEmpty()) {
			messageBuilder.append("<b>Utenti:</b>\n");
			for (UserDto user : users) {
				messageBuilder//
						.append(user.getFirstName()).append(" ")//
						.append(user.getLastName()).append(" - ")//
						.append(user.getUserName()).append(" - ")//
						.append(user.isRegistered() ? "iscritto" : "non iscritto")//
						.append("\n");
			}
		}
	}

}

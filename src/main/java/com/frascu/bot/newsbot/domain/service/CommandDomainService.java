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

	private static final String USERS = "users";
	private static final String CIAO = "Ciao ";
	private static CommandDomainService instance = new CommandDomainService();
	private UserDao userDao = UserDao.getInstance();
	private GroupDao groupDao = GroupDao.getInstance();

	private static final String COMMAND_NOT_EXISTING = "Il comando selezionato non esiste";

	private CommandDomainService() {
		super();
	}

	public static CommandDomainService getInstance() {
		return instance;
	}

	public String startUser(User user) {
		StringBuilder messageBuilder = new StringBuilder();

		if (userDao.isRegistered(user.getId())) {
			messageBuilder.append(CIAO).append(user.getFirstName()).append("\n");
			messageBuilder.append("sei già iscritto.");
		} else {
			userDao.registerUser(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName());
			messageBuilder.append("Benvenuto ").append(user.getFirstName()).append("\n");
			messageBuilder.append("questo bot ti aggiornera' sulle ultime notizie di Sannicandro di Bari.");
		}
		return messageBuilder.toString();
	}

	public String stopUser(User user) {
		StringBuilder messageBuilder = new StringBuilder();

		if (!userDao.isRegistered(user.getId())) {
			messageBuilder.append(CIAO).append(user.getFirstName()).append("\n");
			messageBuilder.append("hai gia' cancellato l'scrizione.");
		} else {
			userDao.unRegisterUser(user.getId());
			messageBuilder.append(CIAO).append(user.getFirstName()).append("\n");
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

			if (strings[0].equals(USERS)) {
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
						.append(user.isRegistered() ? Emoji.VICTORY_HAND : Emoji.CRYING_FACE).append(" - ")//
						.append(user.getUserId()).append(" - ")//
						.append(user.getFirstName()).append(" ")//
						.append("\n");
			}
		}
	}

	public String startGroup(Chat chat) {
		StringBuilder messageBuilder = new StringBuilder();

		if (groupDao.isRegistered(chat.getId())) {
			messageBuilder.append("Ciao \"").append(chat.getTitle()).append("\"\n");
			messageBuilder.append("siete già iscritti alle notizie.");
		} else {
			groupDao.registerGroup(chat.getId());
			messageBuilder.append("Ciao \"").append(chat.getTitle()).append("\"\n");
			messageBuilder.append("questo bot vi aggiornera' sulle ultime notizie di Sannicandro di Bari.");
		}
		return messageBuilder.toString();

	}

	public String stopGroup(Chat chat) {
		StringBuilder messageBuilder = new StringBuilder();

		if (!groupDao.isRegistered(chat.getId())) {
			messageBuilder.append("Ciao a tutti,\navete già cancellato l'iscrizione.");
		} else {
			groupDao.unRegisterGroup(chat.getId());
			messageBuilder.append("Ciao a tutti,\nmi mancherete.");
		}
		return messageBuilder.toString();
	}

}

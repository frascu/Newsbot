package com.frascu.bot.newsbot.telegram.command;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class AdminUsersCommand extends AdminCommand {

	private static final Logger LOGGER = Logger.getLogger(AdminUsersCommand.class);
	public static final String LOGTAG = "ADMINCOMMAND";

	public AdminUsersCommand() {
		super("users", "Invia l'elenco completo degli utenti");
	}

	@Override
	public void executeAdminCommand(AbsSender absSender, User user, Chat chat, String[] strings) {
		String message = commandDomainService.getUsers(strings);

		SendMessage answer = new SendMessage();
		answer.setChatId(chat.getId().toString());
		answer.enableHtml(true);
		answer.setText(message);

		try {
			absSender.sendMessage(answer);
		} catch (TelegramApiException e) {
			LOGGER.error(LOGTAG, e);
		}
	}
}

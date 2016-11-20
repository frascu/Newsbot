package com.frascu.bot.newsbot.telegram.command;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.dao.UserDao;

public class StopCommand extends BotCommand {

	private static final Logger LOGGER = Logger.getLogger(StopCommand.class);
	public static final String LOGTAG = "STOPCOMMAND";

	public StopCommand() {
		super("stop", "Cancella l'iscrizione a questo bot");
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
		UserDao userDao = new UserDao();
		StringBuilder messageBuilder = new StringBuilder();

		String userName = user.getFirstName() + " " + user.getLastName();

		if (!userDao.isRegistered(user.getId())) {
			messageBuilder.append("Ciao ").append(userName).append("\n");
			messageBuilder.append("hai già cancellato l'scrizione.");
		} else {
			userDao.unRegisterUser(user.getId());
			messageBuilder.append("Ciao ").append(userName).append("\n");
			messageBuilder.append("ci mancherai.");
		}

		SendMessage answer = new SendMessage();
		answer.setChatId(chat.getId().toString());
		answer.setText(messageBuilder.toString());

		try {
			absSender.sendMessage(answer);
		} catch (TelegramApiException e) {
			LOGGER.error(LOGTAG, e);
		}
	}
}

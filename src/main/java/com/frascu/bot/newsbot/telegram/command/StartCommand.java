package com.frascu.bot.newsbot.telegram.command;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.telegram.custom.NewsBotCommand;

public class StartCommand extends NewsBotCommand {

	private static final Logger LOGGER = Logger.getLogger(StartCommand.class);
	public static final String LOGTAG = "STARTCOMMAND";

	public StartCommand() {
		super("start", "Ti iscrive alle notizie di questo bot");
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

		String message;
		if (chat.isGroupChat()) {
			message = commandDomainService.startGroup(chat);
		} else {
			message = commandDomainService.startUser(user);
		}

		SendMessage answer = new SendMessage();
		answer.setChatId(String.valueOf(chat.getId()));
		answer.setText(message);

		try {
			absSender.sendMessage(answer);
		} catch (TelegramApiException e) {
			LOGGER.error(LOGTAG, e);
		}
	}

}

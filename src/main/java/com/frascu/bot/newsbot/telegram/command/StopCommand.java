package com.frascu.bot.newsbot.telegram.command;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.domainservice.CommandDomainService;

public class StopCommand extends BotCommand {

	private static final Logger LOGGER = Logger.getLogger(StopCommand.class);
	public static final String LOGTAG = "STOPCOMMAND";

	public StopCommand() {
		super("stop", "Cancella l'iscrizione a questo bot");
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
		String message = CommandDomainService.stop(user);

		SendMessage answer = new SendMessage();
		answer.setChatId(chat.getId().toString());
		answer.setText(message);

		try {
			absSender.sendMessage(answer);
		} catch (TelegramApiException e) {
			LOGGER.error(LOGTAG, e);
		}
	}
}

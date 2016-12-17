package com.frascu.bot.newsbot.telegram.command;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.telegram.BotConfig;
import com.frascu.bot.newsbot.telegram.custom.NewsBotCommand;

public class AdminCommand extends NewsBotCommand {

	private static final Logger LOGGER = Logger.getLogger(AdminCommand.class);
	public static final String LOGTAG = "ADMINCOMMAND";

	public AdminCommand() {
		super("admin", new StringBuilder("Esegue un comando admin:\n").append("\t - <i>users</i> - invia l'elenco completo degli utenti").toString());
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

		if (new Long(BotConfig.ADMIN).equals(chat.getId())) {
			String message = commandDomainService.admin(strings);

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

}

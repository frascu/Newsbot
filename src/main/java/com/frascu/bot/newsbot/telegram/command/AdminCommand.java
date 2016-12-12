package com.frascu.bot.newsbot.telegram.command;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.domainservice.CommandDomainService;
import com.frascu.bot.newsbot.telegram.BotConfig;

public class AdminCommand extends BotCommand {

	private static final Logger LOGGER = Logger.getLogger(AdminCommand.class);
	public static final String LOGTAG = "ADMINCOMMAND";
	private final String info = COMMAND_INIT_CHARACTER + getCommandIdentifier() + "\n" + getDescription();

	public AdminCommand() {
		super("admin", "Esegue un comando admin");
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

		if (new Long(BotConfig.ADMIN).equals(chat.getId())) {
			String message = CommandDomainService.admin(strings);

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

	@Override
	public String toString() {
		return info;
	}

}

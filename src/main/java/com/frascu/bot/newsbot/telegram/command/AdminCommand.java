package com.frascu.bot.newsbot.telegram.command;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

import com.frascu.bot.newsbot.telegram.BotConfig;
import com.frascu.bot.newsbot.telegram.custom.NewsBotCommand;

public abstract class AdminCommand extends NewsBotCommand {

	public static final String LOGTAG = "ADMINCOMMAND";

	public AdminCommand(String command, String description) {
		super(new StringBuilder("admin_").append(command).toString(), description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
		if (BotConfig.ADMIN.equals(chat.getId())) {
			executeAdminCommand(absSender, user, chat, strings);
		}
	}

	abstract public void executeAdminCommand(AbsSender absSender, User user, Chat chat, String[] strings);
}

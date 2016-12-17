package com.frascu.bot.newsbot.telegram.custom;

import org.telegram.telegrambots.bots.commands.BotCommand;

import com.frascu.bot.newsbot.domain.service.CommandDomainService;

public abstract class NewsBotCommand extends BotCommand {

	protected CommandDomainService commandDomainService = CommandDomainService.getInstance();

	private final String info = COMMAND_INIT_CHARACTER + getCommandIdentifier() + "\n" + getDescription();

	public NewsBotCommand(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public String toString() {
		return info;
	}

}

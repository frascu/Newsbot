package com.frascu.bot.newsbot.dto;

import org.telegram.telegrambots.bots.commands.BotCommand;

public class CommandDto {

	private String commandIdentifier;
	private String description;

	public CommandDto(String commandIdentifier, String description) {
		super();
		this.commandIdentifier = commandIdentifier;
		this.description = description;
	}

	public CommandDto() {
		super();
	}

	public String getCommandIdentifier() {
		return commandIdentifier;
	}

	public void setCommandIdentifier(String commandIdentifier) {
		this.commandIdentifier = commandIdentifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return BotCommand.COMMAND_INIT_CHARACTER + getCommandIdentifier() + "\n" + getDescription();
	}

}

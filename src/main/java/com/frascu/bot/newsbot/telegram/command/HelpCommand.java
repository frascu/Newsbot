package com.frascu.bot.newsbot.telegram.command;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * This command helps the user to find the command they need
 *
 * @author Timo Schulz (Mit0x2)
 */
public class HelpCommand extends BotCommand {

	private static final String LOGTAG = "HELPCOMMAND";

	private final ICommandRegistry commandRegistry;

	public HelpCommand(ICommandRegistry commandRegistry) {
		super("help", "Mostra tutti i comandi disponibili");
		this.commandRegistry = commandRegistry;
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

		StringBuilder helpMessageBuilder = new StringBuilder("<b>Aiuto</b>\n");
		helpMessageBuilder.append("I comandi disponibili sono i seguenti:\n\n");

		for (BotCommand botCommand : commandRegistry.getRegisteredCommands()) {
			helpMessageBuilder.append(botCommand.toString()).append("\n\n");
		}

		SendMessage helpMessage = new SendMessage();
		helpMessage.setChatId(chat.getId().toString());
		helpMessage.enableHtml(true);
		helpMessage.setText(helpMessageBuilder.toString());

		try {
			absSender.sendMessage(helpMessage);
		} catch (TelegramApiException e) {
			BotLogger.error(LOGTAG, e);
		}
	}
}

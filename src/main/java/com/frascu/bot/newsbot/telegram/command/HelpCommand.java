package com.frascu.bot.newsbot.telegram.command;

import java.util.List;
import java.util.stream.Collectors;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import com.frascu.bot.newsbot.domainservice.CommandDomainService;

/**
 * This command helps the user to find the command they need
 */
public class HelpCommand extends BotCommand {

	private static final String LOGTAG = "HELPCOMMAND";
	private final String info = COMMAND_INIT_CHARACTER + getCommandIdentifier() + "\n" + getDescription();

	private final ICommandRegistry commandRegistry;

	public HelpCommand(ICommandRegistry commandRegistry) {
		super("help", "Mostra tutti i comandi disponibili");
		this.commandRegistry = commandRegistry;
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

		List<String> commands = commandRegistry.getRegisteredCommands().stream().map(command -> command.toString())
				.collect(Collectors.toList());
		String message = CommandDomainService.help(commands);

		SendMessage helpMessage = new SendMessage();
		helpMessage.setChatId(chat.getId().toString());
		helpMessage.enableHtml(true);
		helpMessage.setText(message);

		try {
			absSender.sendMessage(helpMessage);
		} catch (TelegramApiException e) {
			BotLogger.error(LOGTAG, e);
		}
	}

	@Override
	public String toString() {
		return info;
	}
}

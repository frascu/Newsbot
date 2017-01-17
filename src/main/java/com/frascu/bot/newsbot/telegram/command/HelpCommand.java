package com.frascu.bot.newsbot.telegram.command;

import java.util.List;
import java.util.stream.Collectors;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import com.frascu.bot.newsbot.dto.CommandDto;
import com.frascu.bot.newsbot.telegram.BotConfig;
import com.frascu.bot.newsbot.telegram.custom.NewsBotCommand;

/**
 * This command helps the user to find the command they need
 */
public class HelpCommand extends NewsBotCommand {

	private static final String LOGTAG = "HELPCOMMAND";

	private List<CommandDto> commands;
	/**
	 * The message for the help.
	 */
	private String message;
	private String messageAdmin;

	public HelpCommand(ICommandRegistry commandRegistry) {
		super("help", "Mostra tutti i comandi disponibili");
		commands = commandRegistry.getRegisteredCommands().stream()
				.map(command -> new CommandDto(command.getCommandIdentifier(), command.getDescription()))
				.collect(Collectors.toList());
		commands.add(new CommandDto(this.getCommandIdentifier(), this.getDescription()));
		message = commandDomainService.help(commands, false);
		messageAdmin = commandDomainService.help(commands, true);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

		SendMessage helpMessage = new SendMessage();
		helpMessage.setChatId(chat.getId().toString());
		helpMessage.enableHtml(true);

		if (BotConfig.ADMIN.equals(chat.getId())) {
			helpMessage.setText(messageAdmin);
		} else {
			helpMessage.setText(message);
		}

		try {
			absSender.sendMessage(helpMessage);
		} catch (TelegramApiException e) {
			BotLogger.error(LOGTAG, e);
		}
	}

}

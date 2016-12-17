package com.frascu.bot.newsbot.telegram.handler;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.telegram.BotConfig;
import com.frascu.bot.newsbot.telegram.Emoji;
import com.frascu.bot.newsbot.telegram.command.AdminCommand;
import com.frascu.bot.newsbot.telegram.command.HelpCommand;
import com.frascu.bot.newsbot.telegram.command.StartCommand;
import com.frascu.bot.newsbot.telegram.command.StopCommand;

public class CommandsHandler extends TelegramLongPollingCommandBot {

	private static final Logger LOGGER = Logger.getLogger(CommandsHandler.class);

	/**
	 * Constructor.
	 */
	public CommandsHandler() {
		// register(new HelloCommand());
		register(new StartCommand());
		register(new StopCommand());
		register(new AdminCommand());
		HelpCommand helpCommand = new HelpCommand(this);
		register(helpCommand);

		registerDefaultAction((absSender, message) -> {
			SendMessage commandUnknownMessage = new SendMessage();
			commandUnknownMessage.setChatId(message.getChatId().toString());
			commandUnknownMessage.setText("The command '" + message.getText()
					+ "' is not known by this bot. Here comes some help " + Emoji.AMBULANCE);
			try {
				absSender.sendMessage(commandUnknownMessage);
			} catch (TelegramApiException e) {
				LOGGER.error("Impossible to send the message", e);
			}
			helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[] {});
		});
	}

	@Override
	public String getBotUsername() {
		return BotConfig.NAME;
	}

	@Override
	public void processNonCommandUpdate(Update update) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBotToken() {
		return BotConfig.TOKEN;
	}

}

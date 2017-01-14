package com.frascu.bot.newsbot.telegram.handler;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Contact;
import org.telegram.telegrambots.api.objects.Message;
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

		if (update.hasMessage()) {
			Message message = update.getMessage();

			if (message.hasText()) {
				if (BotConfig.ADMIN.equals(message.getChatId())) {
					sendMessageFromAdminToUser(message);
				} else {
					sendMessageFromUserToAdmin(message);
				}
			}
		}
	}

	private void sendMessageFromUserToAdmin(Message message) {

		// Initialization
		SendMessage echoMessage = new SendMessage();
		StringBuilder messageBuilder = new StringBuilder();
		Contact contact = message.getContact();

		// Build the message
		messageBuilder.append("Nome: ").append(contact.getFirstName()).append("\n");
		messageBuilder.append("Id: ").append(contact.getUserID()).append("\n");
		messageBuilder.append("Messaggio: ").append(message.getText()).append("\n");

		// Set the SendMessage
		echoMessage.setChatId(String.valueOf(BotConfig.ADMIN));
		echoMessage.setText(messageBuilder.toString());

		try {
			sendMessage(echoMessage);
		} catch (TelegramApiException e) {
			LOGGER.error("Impossible to send the message", e);
		}
	}

	private void sendMessageFromAdminToUser(Message message) {

		SendMessage echoMessage = new SendMessage();

		// Split the message from the admin
		String[] splitMessage = message.getText().split("/");

		if (splitMessage.length > 1) {
			String chatId = splitMessage[0];

			String messageToSend = splitMessage[1];

			if (chatId != null && !chatId.isEmpty() && messageToSend != null && !messageToSend.isEmpty()) {
				echoMessage.setChatId(chatId);
				echoMessage.setText(messageToSend);
			}

			try {
				sendMessage(echoMessage);
			} catch (TelegramApiException e) {
				LOGGER.error("Impossible to send the message", e);
			}
		}
	}

	@Override
	public String getBotToken() {
		return BotConfig.TOKEN;
	}

}

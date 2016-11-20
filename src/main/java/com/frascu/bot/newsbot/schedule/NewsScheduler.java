package com.frascu.bot.newsbot.schedule;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.telegram.handler.CommandsHandler;

public class NewsScheduler {

	private static final Logger LOGGER = Logger.getLogger(NewsScheduler.class);

	private Scheduler scheduler;

	public NewsScheduler() throws SchedulerException {
		super();

		// The job that answer to the user
		try {
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
			telegramBotsApi.registerBot(new CommandsHandler());
		} catch (TelegramApiException e) {
			LOGGER.error("Impossible to create the bot.", e);
		}

		// The Job that send the news
		LOGGER.debug("Defining the job");
		JobDetail job = JobBuilder.newJob(NewsJob.class).withIdentity("job1", "group1").build();

		LOGGER.debug("Trigger the job to run now and repeat every 30 seconds");
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(30).repeatForever()).build();

		LOGGER.debug("Creating and starting the scheduler");
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();
		scheduler.scheduleJob(job, trigger);

	}

	public static void main(String[] args) throws ParseException, SchedulerException {
		new NewsScheduler();
	}
}

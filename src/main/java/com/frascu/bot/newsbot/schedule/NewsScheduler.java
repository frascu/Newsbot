package com.frascu.bot.newsbot.schedule;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.telegram.handler.CommandsHandler;

public class NewsScheduler {

	private static final Logger LOGGER = Logger.getLogger(NewsScheduler.class);

	// Unique instance
	private static NewsScheduler newsScheduler = new NewsScheduler();

	// The quartz scheduler
	private Scheduler schedulerQuartz;

	private NewsScheduler() {
		super();
	}

	public static NewsScheduler getInstance() {
		return newsScheduler;
	}

	public void schedule() {

		// The job that answer to the user
		try {
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
			telegramBotsApi.registerBot(new CommandsHandler());
		} catch (TelegramApiException e) {
			LOGGER.error("Impossible to create the bot.", e);
		}
		
		try {
			// The Job that send the news
			LOGGER.debug("Defining the job");
			JobDetail job = JobBuilder.newJob(NewsJob.class).withIdentity("job1", "group1").build();

			LOGGER.debug("Trigger the job to run now and repeat every 30 seconds");
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startNow()
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(120).repeatForever())
					.build();

			LOGGER.debug("Creating and starting the scheduler");

			schedulerQuartz = StdSchedulerFactory.getDefaultScheduler();

			schedulerQuartz.start();

			schedulerQuartz.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			LOGGER.error("Impossible to scheduler the job", e);
		}
	}

	public void unSchedule() {
		try {
			if (schedulerQuartz != null) {
				schedulerQuartz.unscheduleJob(new TriggerKey("trigger1", "group1"));
			}
		} catch (SchedulerException e) {
			LOGGER.error("Impossible to unscheduler the job", e);
		}
	}

	public void stop() {
		try {
			if (schedulerQuartz != null) {
				schedulerQuartz.shutdown(true);
			}
		} catch (SchedulerException e) {
			LOGGER.error("Impossible to shutodown the scheduler", e);
		}
	}

	public static void main(String[] args) {
		NewsScheduler.getInstance().schedule();
	}
}

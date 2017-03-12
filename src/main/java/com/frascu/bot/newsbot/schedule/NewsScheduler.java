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
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.frascu.bot.newsbot.telegram.handler.CommandsHandler;

public class NewsScheduler {

	private static final String TRIGGER1 = "trigger1";
	private static final String JOB1 = "job1";
	private static final String GROUP1 = "group1";

	private static final Logger LOGGER = Logger.getLogger(NewsScheduler.class);

	// Unique instance
	private static NewsScheduler newsScheduler = new NewsScheduler();

	private static final int INTERVAL_SECONDS = 60;

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
			ApiContextInitializer.init();
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
			telegramBotsApi.registerBot(new CommandsHandler());
		} catch (TelegramApiException e) {
			LOGGER.error("Impossible to create the bot.", e);
		}

		try {
			// The Job that send the news
			LOGGER.debug("Defining the job");
			JobDetail job = JobBuilder.newJob(NewsJob.class).withIdentity(JOB1, GROUP1).build();

			LOGGER.debug("Trigger the job to run now and repeat every 30 seconds");
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TRIGGER1, GROUP1).startNow().withSchedule(
					SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(INTERVAL_SECONDS).repeatForever())
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
				schedulerQuartz.unscheduleJob(new TriggerKey(TRIGGER1, GROUP1));
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

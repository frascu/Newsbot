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

public class NewsScheduler {

	private static final Logger LOGGER = Logger.getLogger(NewsScheduler.class);

	private Scheduler scheduler;

	public NewsScheduler() throws SchedulerException {
		super();

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

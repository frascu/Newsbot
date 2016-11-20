package com.frascu.bot.newsbot.dao;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.frascu.bot.newsbot.model.User;

public class UserDao extends DaoBase {

	private static final Logger LOGGER = Logger.getLogger(UserDao.class);

	public void createUser(String userName, String telegramNo) {
		try {
			beginTransaction();

			User user = new User();
			user.setUserName(userName);
			user.setTelegramNo(telegramNo);

			em.persist(user);

			commitTransaction();
		} catch (Exception e) {
			LOGGER.error("Impossible to create the user", e);
			rollbackTransaction();
		} finally {
			try {
				this.close();
			} catch (IOException e) {
				LOGGER.error("Impossible to close the DAO", e);
			}
		}
	}

}

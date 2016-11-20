package com.frascu.bot.newsbot.dao;

import org.apache.log4j.Logger;

import com.frascu.bot.newsbot.model.User;

public class UserDao extends DaoBase {

	private static final Logger LOGGER = Logger.getLogger(UserDao.class);

	public void registerUser(long userId, String userName, String firstName, String lastName) {
		try {
			beginTransaction();

			User user = em.find(User.class, userId);
			if (user == null) {
				user = new User();
				user.setId(userId);
				user.setUserName(userName);
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setRegistered(true);
				em.persist(user);
			} else {
				user.setRegistered(true);
			}

			commitTransaction();

		} catch (

		Exception e) {
			LOGGER.error("Impossible to create the user", e);
			rollbackTransaction();
		}
	}

	public void unRegisterUser(long userId) {
		try {
			if (existUser(userId)) {
				beginTransaction();
				User user = em.find(User.class, userId);
				user.setRegistered(false);
				commitTransaction();
			}
		} catch (Exception e) {
			LOGGER.error("Impossible to create the user", e);
			rollbackTransaction();
		}
	}

	private boolean existUser(long userId) {
		return em.find(User.class, userId) != null;
	}

	public boolean isRegistered(long userId) {
		User user = em.find(User.class, userId);
		return user != null && user.isRegistered();
	}

}

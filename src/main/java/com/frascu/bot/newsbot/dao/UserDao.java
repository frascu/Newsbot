package com.frascu.bot.newsbot.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.frascu.bot.newsbot.dto.UserDto;
import com.frascu.bot.newsbot.model.User;

public class UserDao extends DaoBase {

	private static final Logger LOGGER = Logger.getLogger(UserDao.class);

	private static UserDao instance = new UserDao();

	private UserDao() {
		super();
	}

	public static UserDao getInstance() {
		return instance;
	}

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

	public List<Long> getUserIdsRegistered() {
		String query = new StringBuilder("select id from ").append(User.class.getCanonicalName())
				.append(" where registered = :registered").toString();
		return em.createQuery(query, Long.class).setParameter("registered", true).getResultList();
	}

	public List<UserDto> getAllUsers() {
		String query = new StringBuilder("select user from ").append(User.class.getCanonicalName())
				.append(" user order by registered, firstName, lastName").toString();
		List<User> users = em.createQuery(query, User.class).getResultList();

		return users.stream().map(entity -> new UserDto(entity.getUserName(), entity.getFirstName(),
				entity.getLastName(), entity.isRegistered())).collect(Collectors.toList());
	}

}

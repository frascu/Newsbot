package com.frascu.bot.newsbot.dao;

import java.util.List;
import java.util.stream.Collectors;

import com.frascu.bot.newsbot.dto.UserDto;
import com.frascu.bot.newsbot.model.User;

public class UserDao extends DaoBase {

	public UserDao() {
		super();
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

		} catch (Exception e) {
			LOGGER.error("Impossible to register the user", e);
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
			LOGGER.error("Impossible to unregister the user", e);
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
		return em.createQuery(QueryProvider.QUERY_GET_USER_IDS_REGISTERED, Long.class).setParameter("registered", true)
				.getResultList();
	}

	public List<UserDto> getAllUsers() {
		List<User> users = em.createQuery(QueryProvider.QUERY_GET_ALL_USERS, User.class).getResultList();
		return users.stream().map(entity -> new UserDto(entity.getId(), entity.getUserName(), entity.getFirstName(),
				entity.getLastName(), entity.isRegistered())).collect(Collectors.toList());
	}

}

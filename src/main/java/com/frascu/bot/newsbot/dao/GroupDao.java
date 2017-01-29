package com.frascu.bot.newsbot.dao;

import java.util.List;

import org.apache.log4j.Logger;

import com.frascu.bot.newsbot.model.Group;

public class GroupDao extends DaoBase {

	private static final Logger LOGGER = Logger.getLogger(GroupDao.class);

	public GroupDao() {
		super();
	}

	public void registerGroup(long id) {
		try {
			beginTransaction();

			Group group = em.find(Group.class, id);
			if (group == null) {
				group = new Group();
				group.setId(id);
				group.setRegistered(true);
				em.persist(group);
			} else {
				group.setRegistered(true);
			}

			commitTransaction();

		} catch (Exception e) {
			LOGGER.error("Impossible to register the group", e);
			rollbackTransaction();
		}
	}

	public boolean isRegistered(long id) {
		Group group = em.find(Group.class, id);
		return group != null && group.isRegistered();
	}

	public void unRegisterGroup(long id) {
		try {
			if (existGroup(id)) {
				beginTransaction();
				Group group = em.find(Group.class, id);
				group.setId(id);
				group.setRegistered(false);
				commitTransaction();
			}
		} catch (Exception e) {
			LOGGER.error("Impossible to unregister the group", e);
			rollbackTransaction();
		}
	}

	private boolean existGroup(Long id) {
		return em.find(Group.class, id) != null;
	}

	public List<Long> getGroupIdsRegistered() {
		return em.createQuery(QueryProvider.QUERY_GET_GROUP_IDS_REGISTERED, Long.class).setParameter("registered", true)
				.getResultList();
	}
}

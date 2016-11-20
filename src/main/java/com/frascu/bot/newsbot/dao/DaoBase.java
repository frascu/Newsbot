package com.frascu.bot.newsbot.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DaoBase {

	private static EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("newsbotdb");

	protected static EntityManager em = emfactory.createEntityManager();

	public DaoBase() {
		super();
	}

	protected void beginTransaction() {
		em.getTransaction().begin();
	}

	protected void commitTransaction() {
		em.getTransaction().commit();
	}

	protected void rollbackTransaction() {
		em.getTransaction().rollback();
	}
	
}

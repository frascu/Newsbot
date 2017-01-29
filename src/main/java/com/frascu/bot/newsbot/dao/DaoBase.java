package com.frascu.bot.newsbot.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DaoBase {

	private static EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("newsbotdb");

	protected static EntityManager em = emfactory.createEntityManager();

	private EntityTransaction entityTransaction;

	public DaoBase() {
		super();
	}

	protected void beginTransaction() {
		entityTransaction = em.getTransaction();
		entityTransaction.begin();
	}

	protected void commitTransaction() {
		entityTransaction.commit();
	}

	protected void rollbackTransaction() {
		entityTransaction.rollback();
	}

}

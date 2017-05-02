package com.frascu.bot.newsbot.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

public class DaoBase {

	private static EntityManagerFactory emfactory;
	protected static EntityManager em;
	protected static final Logger LOGGER = Logger.getLogger(DaoBase.class);

	public static void init() {
		try {
			emfactory = Persistence.createEntityManagerFactory("newsbot");
			em = emfactory.createEntityManager();
		} catch (Exception e) {
			LOGGER.error(e);
			System.exit(0);
		}
	}
	
	public static void close() {
		try {
			em.close();
			emfactory.close();
		} catch (Exception e) {
			LOGGER.error(e);
			System.exit(0);
		}
	}

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

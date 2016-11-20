package com.frascu.bot.newsbot.dao;

import java.io.Closeable;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DaoBase implements Closeable {

	private static EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("newsbotdb");

	protected EntityManager em = emfactory.createEntityManager();

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

	@Override
	public void close() throws IOException {
		if (em != null)
			em.close();
	}

}

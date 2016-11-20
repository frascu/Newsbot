package com.frascu.bot.newsbot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_TELEGRAM")
public class User {

	@Id @GeneratedValue
	@Column(name = "USER_ID")
	private long id;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "USER_TELEGRAM_NO")
	private String telegramNo;

	public User() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTelegramNo() {
		return telegramNo;
	}

	public void setTelegramNo(String telegramNo) {
		this.telegramNo = telegramNo;
	}

}

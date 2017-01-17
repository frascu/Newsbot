package com.frascu.bot.newsbot.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GROUP_TELEGRAM")
public class Group {

	@Id
	@Column(name = "GROUP_ID")
	private long id;

	@Column(name = "REGISTERED")
	@Convert(converter = YesNoConverterToBoolean.class)
	private boolean registered;

	public Group() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

}

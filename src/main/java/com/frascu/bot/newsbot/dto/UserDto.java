package com.frascu.bot.newsbot.dto;

public class UserDto {

	private String userName;
	private String firstName;
	private String lastName;
	private boolean registered;

	public UserDto(String userName, String firstName, String lastName, boolean registered) {
		super();
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.registered = registered;
	}

	public UserDto() {
		super();
	}

	public String getUserName() {
		return userName == null ? "" : userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName == null ? "" : firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName == null ? "" : lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

}

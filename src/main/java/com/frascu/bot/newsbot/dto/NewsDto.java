package com.frascu.bot.newsbot.dto;

public class NewsDto {

	private Long id;
	private String link;
	private String title;

	public NewsDto() {
		super();
	}

	public NewsDto(Long id, String link, String title) {
		super();
		this.id = id;
		this.link = link;
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "NewsDto [id=" + id + ", link=" + link + ", title=" + title + "]";
	}
}

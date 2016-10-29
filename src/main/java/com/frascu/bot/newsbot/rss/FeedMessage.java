package com.frascu.bot.newsbot.rss;

import java.util.Date;

public class FeedMessage {

	private String title;
	private String description;
	private String link;
	private String author;
	private String guid;
	private Date pubDate;

	public FeedMessage() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	@Override
	public String toString() {
		return "feed.FeedMessage [title=" + title + ", description=" + description + ", link=" + link + ", author="
				+ author + ", guid=" + guid + "]";
	}

}
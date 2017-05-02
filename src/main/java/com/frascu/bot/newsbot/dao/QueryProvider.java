package com.frascu.bot.newsbot.dao;

import com.frascu.bot.newsbot.model.Group;
import com.frascu.bot.newsbot.model.News;
import com.frascu.bot.newsbot.model.User;

public class QueryProvider {

	// News
	static final String QUERY_GET_COUNT_NEWS_BY_LINK = new StringBuilder("select count(news) from ")
			.append(News.class.getCanonicalName()).append(" news where news.link = :link").toString();

	static final String QUERY_COUNT_NEWS_BY_CREATION_DATE_AND_TITLE = new StringBuilder("select count(*) from ")
			.append(News.class.getCanonicalName())
			.append(" where lower(title) like :word and id <> :id and creationDate > CURRENT_DATE").toString();

	static final String QUERY_GET_NEWS_BY_CREATION_DATE_AND_TITLE = new StringBuilder("select n from ")
			.append(News.class.getCanonicalName())
			.append(" n where lower(n.title) like :word and id <> :id and n.creationDate > CURRENT_DATE").toString();

	// Users
	static final String QUERY_GET_ALL_USERS = new StringBuilder("select user from ")
			.append(User.class.getCanonicalName()).append(" user order by registered desc, firstName, lastName")
			.toString();

	static final String QUERY_GET_USER_IDS_REGISTERED = new StringBuilder("select user.id from ")
			.append(User.class.getCanonicalName()).append(" user where registered = :registered").toString();

	// Groups
	static final String QUERY_GET_GROUP_IDS_REGISTERED = new StringBuilder("select group.id from ")
			.append(Group.class.getCanonicalName()).append(" group where registered = :registered").toString();

}

package com.frascu.bot.newsbot.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import com.frascu.bot.newsbot.dto.NewsDto;
import com.frascu.bot.newsbot.model.News;
import com.frascu.bot.newsbot.rss.FeedMessage;

public class NewsDao extends DaoBase {

	private static final Logger LOGGER = Logger.getLogger(NewsDao.class);

	private static NewsDao instance = new NewsDao();

	private static final String QUERY_GET_COUNT_NEWS_BY_LINK = new StringBuilder("select count(news) from ")
			.append(News.class.getCanonicalName()).append(" news where news.link = :link").toString();

	private NewsDao() {
		super();
	}

	public static NewsDao getInstance() {
		return instance;
	}

	public List<NewsDto> saveNewsFromFeeds(List<FeedMessage> feeds) {
		try {
			beginTransaction();
			List<NewsDto> dtos = feeds.stream().map(feed -> {
				if (!isAlreadyCreated(feed.getLink())) {
					long id = createNews(feed);
					NewsDto dto = new NewsDto();
					dto.setId(id);
					dto.setLink(feed.getLink());
					dto.setTitle(feed.getTitle());
					return dto;
				} else {
					return null;
				}
			}).filter(Objects::nonNull).collect(Collectors.toList());
			commitTransaction();
			return dtos;
		} catch (Exception e) {
			LOGGER.error("Impossible to create the news from feed messages.", e);
			return new ArrayList<>();
		}
	}

	private long createNews(FeedMessage feedMessage) {
		News news = new News();
		news.setLink(feedMessage.getLink());
		news.setTitle(feedMessage.getTitle());
		news.setPubblicationDate(feedMessage.getPubDate());
		news.setCreationDate(new Date());
		em.persist(news);
		em.flush();
		return news.getId();
	}

	private boolean isAlreadyCreated(String link) {
		try {
			return em.createQuery(QUERY_GET_COUNT_NEWS_BY_LINK, Long.class).setParameter("link", link)
					.getSingleResult() == 1;
		} catch (NoResultException e) {
			LOGGER.error("Impossible to get the count of the news by link", e);
			return false;
		}
	}

	public void setNewsAsSent(List<NewsDto> newsDtos) {
		beginTransaction();
		newsDtos.stream().filter(newsDto -> newsDto.getId() != null).forEach(newsDto -> {
			News news = em.find(News.class, newsDto.getId());
			if (news != null) {
				news.setSent(true);
			}
		});
		commitTransaction();
	}
}

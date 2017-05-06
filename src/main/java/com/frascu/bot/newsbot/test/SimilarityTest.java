package com.frascu.bot.newsbot.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.frascu.bot.newsbot.dao.DaoBase;
import com.frascu.bot.newsbot.dao.NewsDao;
import com.frascu.bot.newsbot.dto.NewsDto;
import com.frascu.bot.newsbot.util.NewsUtil;

public class SimilarityTest {

	private static List<NewsDto> news = new ArrayList<>();
	private static NewsDao newsDao;

	@BeforeClass
	public static void init() {
		DaoBase.init();
		newsDao = new NewsDao();
		news.add(new NewsDto(18l, "www.test.it",
				"Sannicandro di Bari - RADICI DEL SUD 2017: OSPITI INTERNAZIONALI E UN FOCUS SUL MERCATO CINESE - PUGLIALIVE.NET"));
		news.add(new NewsDto(12l, "www.test.it",
				"I Cinesi a Radici del Sud 2017 – Sannicandro di Bari, 30 maggio – 5 giugno - NEWSFOOD.com"));
	}

	@AfterClass
	public static void close() {
		DaoBase.close();
	}

	@Test
	public void testSimilarity() {
		List<NewsDto> newsToSend = new ArrayList<>();
		List<NewsDto> newsSimilar = new ArrayList<>();
		NewsUtil.findSimilarNews(newsToSend, newsSimilar, news);

		System.out.println("Similar News:" + newsSimilar);
		System.out.println("News to send:" + newsToSend);
		Assert.assertTrue("The lists are not correct.", (newsSimilar.size() + newsToSend.size()) == 2);
	}

	@Test
	public void testWords() {
		for (NewsDto newsDto : news) {
			String title = NewsUtil.cleanTitle(newsDto.getTitle());
			System.out.println(title);
			List<String> words = NewsUtil.getWords(title);
			for (String word : words) {
				List<NewsDto> similarNews = newsDao.getOtherNewsWithWordToday(word, newsDto.getId());
				System.out.println(word + ": " + similarNews.size());
			}
		}
	}
}

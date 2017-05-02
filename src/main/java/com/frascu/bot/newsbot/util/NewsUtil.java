package com.frascu.bot.newsbot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.frascu.bot.newsbot.dao.NewsDao;
import com.frascu.bot.newsbot.dto.NewsDto;

public class NewsUtil {

	private static NewsDao newsDao = new NewsDao();

	private static final int MIN_OF_N_WORDS_IN_TITLE = 2;
	private static final int LENGHT_OF_WORD_TO_CHECK = 5;

	// TODO Create a list with words to exclude
	private static List<String> wordsToExclude = new ArrayList<>();

	private NewsUtil() {
		super();
	}

	public static void findSimilarNews(List<NewsDto> newsToSend, List<NewsDto> newsSimilar, List<NewsDto> newsDtos) {
		for (NewsDto newsDto : newsDtos) {
			String title = cleanTitle(newsDto.getTitle());
			List<String> words = getWords(title);
			long numberOfWordsInOtherNewsToday = words.stream()
					.filter(word -> newsDao.areOtherNewsWithWordToday(word, newsDto.getId())).count();
			if (numberOfWordsInOtherNewsToday >= MIN_OF_N_WORDS_IN_TITLE) {
				newsSimilar.add(newsDto);
			} else {
				newsToSend.add(newsDto);
			}
		}
	}

	public static List<String> getWords(String title) {
		return Stream.of(title.split(" "))
				.map(word -> word.trim().toLowerCase()).filter(word -> !word.contains(".")
						&& !wordsToExclude.contains(word) && word.length() > LENGHT_OF_WORD_TO_CHECK)
				.collect(Collectors.toList());
	}

	public static String cleanTitle(String title) {
		return title.replaceAll(",", "");
	}

}

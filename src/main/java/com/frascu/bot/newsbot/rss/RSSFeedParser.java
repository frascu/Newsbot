package com.frascu.bot.newsbot.rss;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RSSFeedParser {

	private static final Logger LOGGER = Logger.getLogger(RSSFeedParser.class);

	static final String TITLE = "title";
	static final String DESCRIPTION = "description";
	static final String CHANNEL = "channel";
	static final String LANGUAGE = "language";
	static final String COPYRIGHT = "copyright";
	static final String LINK = "link";
	static final String AUTHOR = "author";
	static final String ITEM = "item";
	static final String PUB_DATE = "pubDate";
	static final String GUID = "guid";

	private List<URL> urls = new ArrayList<>();

	public RSSFeedParser(List<String> feedUrls) {
		for (String feedUrl : feedUrls) {
			try {
				this.urls.add(new URL(feedUrl));
			} catch (MalformedURLException e) {
				LOGGER.error("Bad url", e);
			}
		}
	}

	public List<FeedMessage> readFeed() {
		List<FeedMessage> feed = new ArrayList<>();
		if (!urls.isEmpty()) {
			for (URL url : urls) {
				// First create a new XMLInputFactory
				DocumentBuilderFactory inputFactory = DocumentBuilderFactory.newInstance();

				try {
					// Setup a new eventReader
					InputStream in = openStream(url);
					Document doc = inputFactory.newDocumentBuilder().parse(in);

					// read the XML document
					readNodes(feed, doc.getElementsByTagName(ITEM));

				} catch (SAXException | IOException | ParserConfigurationException e) {
					LOGGER.error("Exception parsing the document", e);
				}
			}
		}
		return feed;
	}

	private void readNodes(List<FeedMessage> feed, NodeList nList) {
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				FeedMessage feedMessage = new FeedMessage();
				feedMessage.setTitle(getCharacterData(element, TITLE));
				feedMessage.setAuthor(getCharacterData(element, AUTHOR));
				feedMessage.setLink(getLink(element));
				feedMessage.setDescription(getCharacterData(element, DESCRIPTION));

				// Get publication date
				DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
				try {
					fillPubblicationDate(element, feedMessage, format);

					// Add only if the publication date exists
					feed.add(feedMessage);
				} catch (ParseException e) {
					LOGGER.error("Impossible to parse the publication date", e);
				}
			}
		}
	}

	private void fillPubblicationDate(Element element, FeedMessage feedMessage, DateFormat format)
			throws ParseException {
		String contentDate = getCharacterData(element, PUB_DATE);
		if (contentDate != null && !contentDate.isEmpty()) {
			feedMessage.setPubDate(format.parse(getCharacterData(element, PUB_DATE)));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("DATE: " + feedMessage.getPubDate() + ", TITLE: " + feedMessage.getTitle());
			}
		}
	}

	private String getLink(Element element) {
		String url = getCharacterData(element, LINK);

		if (url == null || url.isEmpty()) {
			return getCharacterData(element, GUID);
		}

		String contentLink = getCharacterData(element, GUID);
		if (contentLink != null && contentLink.contains("google")) {
			String[] splitlink = contentLink.split("cluster=");
			if (splitlink.length > 1) {
				return splitlink[1].substring(0);
			}
		}
		return url;
	}

	private String getCharacterData(Element element, String tagName) {
		try {
			return element.getElementsByTagName(tagName).item(0).getFirstChild().getNodeValue();
		} catch (NullPointerException ne) {
			LOGGER.debug(new StringBuilder(tagName).append(" doesn't exist"), ne);
			return "";
		}
	}

	private InputStream openStream(URL url) throws IOException {
		if ("https".equalsIgnoreCase(url.getProtocol())) {
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			return con.getInputStream();
		} else {
			return url.openStream();
		}
	}
}
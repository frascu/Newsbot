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

	private URL url;

	public RSSFeedParser(String feedUrl) {
		try {
			this.url = new URL(feedUrl);
		} catch (MalformedURLException e) {
			LOGGER.error("Bad url", e);
		}
	}

	public List<FeedMessage> readFeed() {
		List<FeedMessage> feed = new ArrayList<>();
		if (url != null) {
			// First create a new XMLInputFactory
			DocumentBuilderFactory inputFactory = DocumentBuilderFactory.newInstance();

			try {
				// Setup a new eventReader
				InputStream in = read();
				Document doc = inputFactory.newDocumentBuilder().parse(in);

				// read the XML document
				readNodes(feed, doc.getElementsByTagName(ITEM));

			} catch (SAXException | IOException | ParserConfigurationException e) {
				LOGGER.error("Exception parsing the document", e);
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
				feedMessage.setLink(getCharacterData(element, LINK));
				feedMessage.setDescription(getCharacterData(element, DESCRIPTION));
				feedMessage.setGuid(getCharacterData(element, GUID));

				// Get publication date
				DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss +0000", Locale.ENGLISH);
				try {
					feedMessage.setPubDate(format.parse(getCharacterData(element, PUB_DATE)));
				} catch (ParseException e) {
					LOGGER.error("Impossible to parse the publication date", e);
				}

				LOGGER.debug(new StringBuilder("DATE: ").append(feedMessage.getPubDate()).append(", TITLE: ")
						.append(feedMessage.getTitle()).toString());

				feed.add(feedMessage);
			}
		}
	}

	private String getCharacterData(Element element, String tagName) {
		return element.getElementsByTagName(tagName).item(0).getTextContent();
	}

	private InputStream read() throws IOException {
		return url.openStream();
	}
}
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RSSFeedParser {
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

	final URL url;

	public RSSFeedParser(String feedUrl) {
		try {
			this.url = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<FeedMessage> readFeed() {
		List<FeedMessage> feed = new ArrayList<FeedMessage>();

		// First create a new XMLInputFactory
		DocumentBuilderFactory inputFactory = DocumentBuilderFactory.newInstance();
		// Setup a new eventReader
		InputStream in = read();
		Document doc;
		try {
			doc = inputFactory.newDocumentBuilder().parse(in);

			// read the XML document
			NodeList nList = doc.getElementsByTagName(ITEM);
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
						feedMessage.setPubDate(null);
					}

					// Show the date and the title of the feed message
					System.out.println(new StringBuilder("DATE: ").append(feedMessage.getPubDate()).append(", TITLE: ")
							.append(feedMessage.getTitle()).toString());

					feed.add(feedMessage);
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return feed;
	}

	private String getCharacterData(Element element, String tagName) {
		return element.getElementsByTagName(tagName).item(0).getTextContent();
	}

	private InputStream read() {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
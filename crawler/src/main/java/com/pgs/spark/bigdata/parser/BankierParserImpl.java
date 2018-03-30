package com.pgs.spark.bigdata.parser;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component(value = "bankierParser")
public class BankierParserImpl extends WebPageParser {

	private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	@Override
	public String getParsablePage() {
		return "bankier.pl";
	}

	@Override
	protected String getTitle(Document document) {
		return document.select("article header .entry-title").text();
	}

	@Override
	protected Set<String> getTags(Document document) {
		Set<String> tags = document.select("article footer #articleTag .boxContent")
				.stream()
				.map(Element::text)
				.collect(Collectors.toSet());
		return tags;
	}

	@Override
	protected String getDate(Document document) {
		return document.select("article header time").attr("datetime");
	}

	@Override
	protected String getContent(Document document) {
		final List<Element> articleElements = document.getElementsByTag("article");
		return articleElements.stream().map(Element::text).collect(Collectors.joining());
	}

	@Override
	protected SimpleDateFormat getDateFormat() {
		return format;
	}
}

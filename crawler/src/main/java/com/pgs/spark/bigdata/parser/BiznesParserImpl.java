package com.pgs.spark.bigdata.parser;

import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component(value = "biznesParser")
public class BiznesParserImpl extends WebPageParser {
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ssZ");
	
	@Override
	public String getParsablePage() {
		return "biznes.pl";
	}

	@Override
	protected String getTitle(Document document) {
		return document.select("#articleHeading #mainTitle").text().trim();
	}

	@Override
	protected Set<String> getTags(Document document) {
		return document.select("#articleDetail #main #relatedTopics").stream()
				.filter(element -> !element.text().contains("Tematy:"))
				.map(Element::text)
				.collect(Collectors.toSet());
	}

	@Override
	protected String getDate(final Document document) {
		return document.select("#articleHeading meta").attr("content");
	}

	@Override
	protected String getContent(final Document document) {
		final Elements elements = document.select("#articleDetail #main");
		return elements.stream().map(Element::text).collect(Collectors.joining());
	}

	@Override
	protected SimpleDateFormat getDateFormat() {
		return format;
	}
}

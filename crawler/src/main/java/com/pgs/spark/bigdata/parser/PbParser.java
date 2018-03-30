package com.pgs.spark.bigdata.parser;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component(value = "pbParser")
public class PbParser extends WebPageParser {

	private final String inTheClipboardSentence = "w schowku Tylko dla zalogowanych czytelników zaloguj się lub zarejestrujdodaj do schowka ";
	private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");

	@Override
	public String getParsablePage() {
		return "pb.pl";
	}

	@Override
	protected String getTitle(Document document) {
		return document.select(".article h1").text();
	}

	@Override
	protected Set<String> getTags(Document document) {
		return Collections.emptySet();
	}

	@Override
	protected String getDate(Document document) {
		return document.select("article span.article_date").text();
	}

	@Override
	protected String getContent(Document document) {
		final List<Element> articleElements = document.getElementsByClass("article");
		return articleElements.stream().filter(element -> !element.text().isEmpty()).map(Element::text).map(text -> {
			return text.contains(inTheClipboardSentence) ? text.substring(text.indexOf(inTheClipboardSentence) + inTheClipboardSentence.length()):text;
		}).collect(Collectors.joining());
	}

	@Override
	protected SimpleDateFormat getDateFormat() {
		return format;
	}
}

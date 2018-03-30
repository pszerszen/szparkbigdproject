package com.pgs.spark.bigdata.parser;

import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component(value = "wpParser")
public class WpParserImpl extends WebPageParser {
    
	private final SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd'T'hh:mm");

	@Override
    public String getParsablePage() {
        return "wp.pl";
    }

	@Override
	protected String getTitle(Document document) {
		String title = document.select(".article .title").text();
		if(StringUtils.isEmpty(title)){
			title = document.select("article section.artCnt header div.h1").text();
		}
		return title;
	}

	@Override
	protected Set<String> getTags(Document document) {
		return document.select("article section.artCnt main div.tags_small")
				.stream()
				.map(Element::text)
				.collect(Collectors.toSet());
	}

	@Override
	protected String getDate(Document document) {
		return document.select("article section.artCnt header time").attr("datetime");
	}

	@Override
	protected String getContent(Document document) {
		final Elements select = document.select("article section.artCnt");
		final String header = select.select("header div.h1").text();
		final Elements main = select.select("main");
		final String lead = main.select("div.lead").text();
		final String intertext = main.select("div.intertext1").text();
		return StringUtils.join(header, lead, intertext);
	}

	@Override
	protected SimpleDateFormat getDateFormat() {
		return format ;
	}
}

package com.pgs.spark.bigdata.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.pgs.spark.bigdata.to.ArticleTO;

import edu.uci.ics.crawler4j.parser.HtmlParseData;

public abstract class WebPageParser {

	protected abstract String getTitle(Document document);

	protected abstract Set<String> getTags(Document document);

	protected abstract String getDate(Document document);

	protected abstract String getContent(Document document);
	
	protected abstract SimpleDateFormat getDateFormat();
	
	public abstract String getParsablePage();

	public ArticleTO parse(HtmlParseData data) {
		final String htmlData = data.getHtml().replaceAll("(\\r|\\n)", "");
		final Document document = Jsoup.parse(htmlData);
		final String content = getContent(document);
		final String date = getDate(document);
		final String title = getTitle(document);
		final Set<String> tags = getTags(document);
		final ArticleTO article = new ArticleTO();
		article.setContent(content);
		article.setTitle(title);
		article.setTags(tags);
		try {
			article.setDate(getDateFormat().parse(date));
		} catch (final ParseException e) {
			article.setDate(new Date());
		}
		return article;
	}

}

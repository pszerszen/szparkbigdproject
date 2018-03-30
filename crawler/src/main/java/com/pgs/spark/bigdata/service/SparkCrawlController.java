package com.pgs.spark.bigdata.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.pgs.spark.bigdata.config.CrawlerConfiguration;
import com.pgs.spark.bigdata.parser.WebPageParser;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class SparkCrawlController extends CrawlController {
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private CrawlerConfiguration crawlerConfiguration;
	
    @Autowired
    private List<WebPageParser> parsers;

	public SparkCrawlController(CrawlConfig config, PageFetcher pageFetcher, RobotstxtServer robotstxtServer) throws Exception {
		super(config, pageFetcher, robotstxtServer);
	}
	
	public DocumentService getDocumentService() {
		return documentService;
	}
	
	public CrawlerConfiguration getCrawlerConfiguration() {
		return crawlerConfiguration;
	}
	
	public List<WebPageParser> getParsers() {
		return parsers;
	}
	
	public Map<String, WebPageParser> getParsersMap() {
		return parsers.stream().collect(Collectors.toMap(WebPageParser::getParsablePage, Function.identity()));
	}

}

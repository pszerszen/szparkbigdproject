package com.pgs.spark.bigdata.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.pgs.spark.bigdata.service.SparkCrawlController;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Configuration
@PropertySource("classpath:crawler.properties")
public class CrawlerConfiguration {

    @Value("${crawler.storageFolder}")
    private String storageFolder;

    @Value("${crawler.politenessDelay}")
    private int politnessDelay;

    @Value("${crawler.maxDepthOfCrawling}")
    private int maxDepthOfCrawling;

    @Value("${crawler.maxPagesToFetch}")
    private int maxPagesToFetch;

    @Value("${crawler.connectionTimeout}")
    private int connectionTimeout;

    @Value("${crawler.socketTimeout}")
    private int socketTimeout;

    @Value("${crawler.includeBinaryContentInCrawling}")
    private boolean includeBinaryContentInCrawling;

    @Value("${crawler.resumableCrawling}")
    private boolean resumableCrawling;

    @Value("#{'${crawler.urlSeeds}'.split(',')}")
    private List<String> urlSeeds;

    @Bean
    public CrawlConfig getCrawlerConfiguration() {
        final CrawlConfig config = new CrawlConfig();

        config.setCrawlStorageFolder(storageFolder);
        config.setPolitenessDelay(politnessDelay);
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        config.setMaxPagesToFetch(maxPagesToFetch);
        config.setConnectionTimeout(connectionTimeout);
        config.setIncludeBinaryContentInCrawling(includeBinaryContentInCrawling);
        config.setResumableCrawling(resumableCrawling);
        config.setSocketTimeout(socketTimeout);
        config.setUserAgentString("");

        return config;
    }

    @Bean
    @Autowired
    public PageFetcher pageFetcher(CrawlConfig config){
        return new PageFetcher(config);
    }

    @Bean
    public RobotstxtConfig robotstxtConfig(){
        return new RobotstxtConfig();
    }

    @Bean
    @Autowired
    public RobotstxtServer robotstxtServer(RobotstxtConfig robotstxtConfig, PageFetcher pageFetcher){
        return new RobotstxtServer(robotstxtConfig, pageFetcher);
    }

    @Bean
    @Autowired
    public SparkCrawlController crawlController(CrawlConfig config, PageFetcher pageFetcher, RobotstxtServer server) throws Exception {
        final SparkCrawlController controller = new SparkCrawlController(config, pageFetcher, server);
        urlSeeds.forEach(controller::addSeed);
        return controller;
    }
    
    public List<String> getUrlSeeds() {
		return urlSeeds;
	}

}

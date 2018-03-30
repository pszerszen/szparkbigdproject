package com.pgs.spark.bigdata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.pgs.spark.bigdata.crawler.BasicCrawler;

@Service
@PropertySource("classpath:crawler.properties")
public class CrawlerServiceImpl implements CrawlerService {

    @Value("${crawler.numberOfCrawlers}")
    private int numberOfCrawlers;

    @Autowired
    private SparkCrawlController controller;

    @Override
	public void crawl() throws Exception {
        controller.start(BasicCrawler.class, numberOfCrawlers);
    }

}

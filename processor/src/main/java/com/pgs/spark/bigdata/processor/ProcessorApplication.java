package com.pgs.spark.bigdata.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import com.pgs.spark.bigdata.processor.domain.MultilayerPerceptronClassifierProperties;

@SpringBootApplication
@ComponentScan(basePackageClasses = ProcessorApplication.class)
@EnableConfigurationProperties({MultilayerPerceptronClassifierProperties.class})
@EnableSpringDataWebSupport
@EnableJpaRepositories("com.pgs.spark.bigdata.processor.repository")
public class ProcessorApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ProcessorApplication.class, args);
    }

}

package com.pgs.spark.bigdata.algorithmComparator.config;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

    @Bean
    public JavaSparkContext getSparkContext(){
        return new JavaSparkContext("local[4]", "Simple app");
    }

    @Bean
    @Autowired
    public SQLContext getSqlContext(JavaSparkContext javaSparkContext){
        return new SQLContext(javaSparkContext);
    }
}

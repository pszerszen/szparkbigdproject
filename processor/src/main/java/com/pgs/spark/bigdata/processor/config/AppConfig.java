package com.pgs.spark.bigdata.processor.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.reflect.ClassPath;
import com.pgs.spark.bigdata.processor.jobs.SparkJob;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class AppConfig {

    @Value("${crossOrigin.origins}")
    String[] allowedOrigins;

    @Bean
    public ImmutableMap<String, SparkJob> sparkJobProvider(ApplicationContext context) throws IOException {
        ImmutableMap.Builder<String, SparkJob> builder = new Builder<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        ClassPath.from(loader).getTopLevelClasses(SparkJob.class.getPackage().getName()).stream()
                .forEach(classInfo -> {
                    final Class<?> aClass = classInfo.load();
                    if (SparkJob.class.isAssignableFrom(aClass) && !SparkJob.class.equals(aClass)) {
                        final Object bean = context.getBean(aClass);
                        builder.put(aClass.getSimpleName(), (SparkJob) bean);
                    }
                });

        return builder.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        for (String allowedOrigin : allowedOrigins) {
            config.addAllowedOrigin(allowedOrigin);
        }
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


}

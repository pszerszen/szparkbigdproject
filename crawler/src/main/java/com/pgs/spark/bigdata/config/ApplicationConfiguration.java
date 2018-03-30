package com.pgs.spark.bigdata.config;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:facebook.properties")
public class ApplicationConfiguration {

    @Value("${fb.appId}")
    private String appId;

    @Value("${fb.appSecret}")
    private String appSecret;

    @Value("${fb.permissions}")
    private String permissions;

    @Value("${fb.accessToken}")
    private String accessToken;

    @Bean
    public Facebook facebook() {
        Facebook facebook = new FacebookFactory().getInstance();

        facebook.setOAuthAppId(appId, appSecret);
        facebook.setOAuthPermissions(permissions);
        facebook.setOAuthAccessToken(new AccessToken(accessToken, null));

        return facebook;
    }
}

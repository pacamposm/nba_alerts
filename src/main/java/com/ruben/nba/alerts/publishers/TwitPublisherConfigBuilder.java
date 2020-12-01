package com.ruben.nba.alerts.publishers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import twitter4j.conf.ConfigurationBuilder;

@Component
public class TwitPublisherConfigBuilder {

    @Value( "${twitter.bot.consumerKey}" )
    private String consumerKey;

    @Value( "${twitter.bot.consumerSecret}" )
    private String consumerSecret;

    @Value( "${twitter.bot.accessToken}" )
    private String accessToken;

    @Value( "${twitter.bot.accessTokenSecret}" )
    private String accessTokenSecret;


    public ConfigurationBuilder getConfigBuilder() {

        ConfigurationBuilder twitterConfigBuilder = new ConfigurationBuilder();
        twitterConfigBuilder.setDebugEnabled(true);
        twitterConfigBuilder.setOAuthConsumerKey(consumerKey);
        twitterConfigBuilder.setOAuthConsumerSecret(consumerSecret);
        twitterConfigBuilder.setOAuthAccessToken(accessToken);
        twitterConfigBuilder.setOAuthAccessTokenSecret(accessTokenSecret);

        return twitterConfigBuilder;
    }
}
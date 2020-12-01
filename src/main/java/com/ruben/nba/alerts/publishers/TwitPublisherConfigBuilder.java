package com.ruben.nba.alerts.publishers;

import org.springframework.stereotype.Component;
import twitter4j.conf.ConfigurationBuilder;

@Component
public class TwitPublisherConfigBuilder {

    public ConfigurationBuilder getConfigBuilder() {

        ConfigurationBuilder twitterConfigBuilder = new ConfigurationBuilder();
        twitterConfigBuilder.setDebugEnabled(true);
        twitterConfigBuilder.setOAuthConsumerKey("xxxxxx");
        twitterConfigBuilder.setOAuthConsumerSecret("xxxxxx");
        twitterConfigBuilder.setOAuthAccessToken("xxxxxx");
        twitterConfigBuilder.setOAuthAccessTokenSecret("xxxxxx");

        return twitterConfigBuilder;
    }
}

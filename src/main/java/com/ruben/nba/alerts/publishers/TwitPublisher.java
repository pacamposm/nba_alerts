package com.ruben.nba.alerts.publishers;

import com.ruben.nba.alerts.bootstrap.Message;
import com.ruben.nba.alerts.repositories.ImagesRepository;
import org.springframework.stereotype.Component;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;

@Component
public class TwitPublisher {

    private TwitPublisherConfigBuilder twitPublisherConfig;
    private ImagesRepository imagesRepository;

    public TwitPublisher(TwitPublisherConfigBuilder twitPublisherConfig, ImagesRepository imagesRepository) {
        this.twitPublisherConfig = twitPublisherConfig;
        this.imagesRepository = imagesRepository;
    }

    public void publish(Message message) throws TwitterException {

        ConfigurationBuilder configBuilder = twitPublisherConfig.getConfigBuilder();

        Twitter twitter = new TwitterFactory(configBuilder.build()).getInstance();

        String statusMessage = message.getText();
        File file = imagesRepository.get(message.getPersonId());

        StatusUpdate status = new StatusUpdate(statusMessage);
        status.setMedia(file);
        twitter.updateStatus(status);
    }
}

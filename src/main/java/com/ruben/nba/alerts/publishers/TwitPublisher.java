package com.ruben.nba.alerts.publishers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.batik.transcoder.TranscoderException;
import org.springframework.stereotype.Component;

import com.ruben.nba.alerts.data.Message;
import com.ruben.nba.alerts.repositories.ImagesRepository;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UploadedMedia;
import twitter4j.conf.ConfigurationBuilder;

@Component
public class TwitPublisher {
    private static final int MAX_IMAGES_ALLOWED = 4;

    private ImagesRepository imagesRepository;
    private Twitter twitter;

    public TwitPublisher(TwitPublisherConfigBuilder twitPublisherConfig, ImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
        this.twitter = initTwitter(twitPublisherConfig);
    }

    private Twitter initTwitter(TwitPublisherConfigBuilder twitPublisherConfig) {
        ConfigurationBuilder configBuilder = twitPublisherConfig.getConfigBuilder();
        return new TwitterFactory(configBuilder.build()).getInstance();
    }

    public void publish(Message message) throws TwitterException, IOException, TranscoderException {            
            String statusMessage = message.getText();
            StatusUpdate status = new StatusUpdate(statusMessage);

            long[] mediaIds = getImageIds(message, twitter);
            status.setMediaIds(mediaIds);

            twitter.updateStatus(status);
    }

    private long[] getImageIds(Message message, Twitter twitter) throws TwitterException, IOException, TranscoderException {
        long[] mediaIds;

        List<String> secondaryImages = message.getSecondaryImages();
        int elements = Integer.max(MAX_IMAGES_ALLOWED, 1 + secondaryImages.size());
        
        mediaIds = new long[elements];
        File fistFile = getFirstFile(message);
        UploadedMedia media1 = twitter.uploadMedia(fistFile);
        mediaIds[0] = media1.getMediaId();
        
        for (int i=1; i < elements; i++) {
            String imagePath = secondaryImages.get(i -1);
            File secondaryFile = imagesRepository.getSecondPlayer(imagePath);
            UploadedMedia secondaryMedia = twitter.uploadMedia(secondaryFile);
            mediaIds[i] = secondaryMedia.getMediaId();
        }
        
        return mediaIds;
    }

    private File getFirstFile(Message message) throws IOException, TranscoderException {
        File file;
        if ("0".equals(message.getPersonId()) || "-1".equals(message.getPersonId())) {
            file = imagesRepository.getTeam(message.getTeamId());
        } else {
            file = imagesRepository.getPlayer(message.getPersonId());
        }

        return file;
    }
}

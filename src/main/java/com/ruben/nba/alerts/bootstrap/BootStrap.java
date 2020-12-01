package com.ruben.nba.alerts.bootstrap;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ruben.nba.alerts.filters.AlertsFilter;
import com.ruben.nba.alerts.mappers.MessageMapper;
import com.ruben.nba.alerts.publishers.TwitPublisher;
import com.ruben.nba.alerts.repositories.ImagesRepository;
import com.ruben.nba.alerts.repositories.NbaStatsRepository;
import com.ruben.nba.alerts.repositories.PublishedIdsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class BootStrap implements CommandLineRunner {

    private final NbaStatsRepository nbaStatsRepository;
    private final ImagesRepository imagesRepository;
    private final AlertsFilter alertsFilter;
    private final MessageMapper messageMapper;
    private final TwitPublisher twitPublisher;
    private final PublishedIdsRepository publishedIdsRepository;

    public BootStrap(NbaStatsRepository nbaStatsRepository, AlertsFilter alertsFilter, MessageMapper messageMapper,
                     ImagesRepository imagesRepository, TwitPublisher twitPublisher,
                     PublishedIdsRepository publishedIdsRepository) {

        this.nbaStatsRepository = nbaStatsRepository;
        this.alertsFilter = alertsFilter;
        this.messageMapper = messageMapper;
        this.imagesRepository = imagesRepository;
        this.twitPublisher = twitPublisher;
        this.publishedIdsRepository = publishedIdsRepository;
    }

    @Override
    public void run(String... args) {

        try {
            initPublishedList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        scheduleTask();
    }

    private void initPublishedList() throws IOException {
        List<Message> messages = getUnpublished();

        for (Message message: messages) {
            publishedIdsRepository.add(message.getMessageId());
        }
    }

    public void scheduleTask() {

        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    askForAlertsAndPublish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Timer timer = new Timer("Timer");
        timer.schedule(task, 0, 30000);

    }

    private void askForAlertsAndPublish() throws IOException {
        List<Message> messages = getUnpublished();

        System.out.println("Mensajes a publicar " + messages.size());

        for (Message message : messages) {
            try {
                downloadImageIfNotExists(message);
                twitPublisher.publish(message);
                publishedIdsRepository.add(message.getMessageId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<Message> getUnpublished() throws IOException {

        ArrayNode nbaStatsResponse = (ArrayNode) nbaStatsRepository.getAll().get("alerts");

        return StreamSupport.stream(nbaStatsResponse.spliterator(), true)
                .filter(alertsFilter.getFilter())
                .map(messageMapper::map)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    private void downloadImageIfNotExists(Message message) throws IOException {
        String playerId = message.getPersonId();

        if (!imagesRepository.exists(playerId)) {
            byte[] imageBytes = nbaStatsRepository.getImage(playerId);
            imagesRepository.save(playerId, imageBytes);
        }
    }
}

package com.ruben.nba.alerts.bootstrap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ruben.nba.alerts.filters.AlertsFilter;
import com.ruben.nba.alerts.mappers.MessageMapper;
import com.ruben.nba.alerts.publishers.TwitPublisher;
import com.ruben.nba.alerts.repositories.ImagesRepository;
import com.ruben.nba.alerts.repositories.NbaStatsRepository;
import com.ruben.nba.alerts.repositories.PublishedIdsRepository;
import org.apache.batik.transcoder.TranscoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class BootStrap implements CommandLineRunner {


    private static final Logger LOGGER = LoggerFactory.getLogger(BootStrap.class);

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
            LOGGER.info("Comenzamos");
            initPublishedList();
        } catch (Exception e) {
            LOGGER.error("Se ha producido un error", e);
        }

        scheduleTask();
    }

    private void initPublishedList() throws IOException {
        List<Message> messages = getUnpublished();

        for (Message message: messages) {
            publishedIdsRepository.add(message);
        }
    }

    public void scheduleTask() {

        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    askForAlertsAndPublish();
                } catch (Exception e) {
                    LOGGER.error("Se ha producido un error", e);
                }
            }
        };

        Timer timer = new Timer("PublishAlerts");
        timer.schedule(task, 0, 30000);

    }

    private void askForAlertsAndPublish() throws IOException {
        List<Message> messages = getUnpublished();

        LOGGER.info("Mensajes a publicar " + messages.size());

        for (Message message : messages) {
            try {
                downloadImageIfNotExists(message);
                twitPublisher.publish(message);
                publishedIdsRepository.add(message);
            } catch (Exception e) {
                LOGGER.warn(messages.toString());
                LOGGER.warn("No ha podido publicar el mensaje", e);
            }
        }
    }

    private List<Message> getUnpublished() throws IOException {

        ArrayNode nbaStatsResponse = (ArrayNode) nbaStatsRepository.getAll().get("alerts");

        return StreamSupport.stream(nbaStatsResponse.spliterator(), true)
                .sorted(getComparator())
                .map(messageMapper::map)
                .filter(alertsFilter.getFilter())
                .collect(Collectors.toCollection(ArrayList::new));

    }

    private Comparator<JsonNode> getComparator() {
        return (json1, json2) -> {
            int visit1 = json1.get("VisitorTeamScore").asInt(0);
            int visit2 = json2.get("VisitorTeamScore").asInt(0);
            int difference = visit1 - visit2;
            if (difference != 0) {
                return difference;
            } else {
                int home1 = json1.get("HomeTeamScore").asInt(0);
                int home2 = json2.get("HomeTeamScore").asInt(0);
                return home1 - home2;
            }
        };
    }

    private void downloadImageIfNotExists(Message message) throws IOException, TranscoderException {
        String playerId = message.getPersonId();

        if ("0".equals(playerId) || "-1".equals(playerId)) {
            String teamId = message.getTeamId();
            downloadTeamImageIfNotExists(teamId);
        } else {
            downloadPlayerImageIfNotExists(playerId);
        }
    }

    private void downloadPlayerImageIfNotExists(String playerId) throws IOException {
        if (!imagesRepository.existsPlayer(playerId)) {
            byte[] imageBytes = nbaStatsRepository.getPlayerImage(playerId);
            imagesRepository.savePlayer(playerId, imageBytes);
        }
    }

    private void downloadTeamImageIfNotExists(String teamId) throws IOException, TranscoderException {
        if (!imagesRepository.existsTeam(teamId)) {
            String teamImageUrl = nbaStatsRepository.getTeamImageURL(teamId);
            imagesRepository.saveTeam(teamId, teamImageUrl);
        }
    }




}

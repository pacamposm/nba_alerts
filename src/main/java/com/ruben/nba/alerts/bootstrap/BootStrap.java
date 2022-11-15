package com.ruben.nba.alerts.bootstrap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ruben.nba.alerts.data.Message;
import com.ruben.nba.alerts.filters.AlertsFilter;
import com.ruben.nba.alerts.mappers.MessageMapper;
import com.ruben.nba.alerts.publishers.TwitPublisher;
import com.ruben.nba.alerts.repositories.PublishedMessagesRepository;
import com.ruben.nba.alerts.service.NbaStatsService;
import com.ruben.nba.alerts.utils.LambdaExceptionUtil;

@Component
public class BootStrap implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootStrap.class);

    private final NbaStatsService nbaStatsService;
    private final AlertsFilter alertsFilter;
    private final MessageMapper messageMapper;
    private final TwitPublisher twitPublisher;
    private final PublishedMessagesRepository publishedIdsRepository;

    public BootStrap(NbaStatsService nbaStatsService, AlertsFilter alertsFilter, MessageMapper messageMapper,
            TwitPublisher twitPublisher, PublishedMessagesRepository publishedIdsRepository) {

        this.nbaStatsService = nbaStatsService;
        this.alertsFilter = alertsFilter;
        this.messageMapper = messageMapper;
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
        publishedIdsRepository.addAll(messages);
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
                twitPublisher.publish(message);
                publishedIdsRepository.add(message);
            } catch (Exception e) {
                LOGGER.warn(messages.toString());
                LOGGER.warn("No ha podido publicar el mensaje", e);
            }
        }
    }

    private List<Message> getUnpublished() throws IOException {

        ArrayNode nbaStatsResponse = (ArrayNode) nbaStatsService.getAll().get("alerts");

        return StreamSupport.stream(nbaStatsResponse.spliterator(), true).sorted(getComparator())
                .map(LambdaExceptionUtil.rethrowFunction(messageMapper::map)).filter(alertsFilter.getFilter())
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

}

package com.ruben.nba.alerts.mappers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.opencsv.exceptions.CsvException;
import com.ruben.nba.alerts.data.Message;
import com.ruben.nba.alerts.repositories.ImagesRepository;

@Component
public class MessageMapper {

    @Autowired
    ImagesRepository imagesRepository;

    private final static String SOURCE_TEXT = "source: NBA alerts";

    public Message map(JsonNode jsonObject) throws FileNotFoundException, IOException, CsvException {
        Message message = new Message();

        message.setMessageId(getAlertId(jsonObject));
        message.setPersonId(getPersonId(jsonObject));
        message.setTeamId(getTeamId(jsonObject));
        message.setPersonName(getPersonName(jsonObject));
        message.setText(getMessageFullText(jsonObject));
        message.setSecondaryImage(getSecondaryImages(jsonObject));

        return message;
    }

    private String getAlertId(JsonNode jsonObject) {
        return jsonObject.get("AlertId").asText();
    }

    private String getTeamId(JsonNode jsonObject) {
        return jsonObject.get("TeamId").asText();
    }

    private String getPersonId(JsonNode jsonObject) {
        return jsonObject.get("PersonId").asText();
    }

    private String getPersonName(JsonNode jsonObject) {
        return jsonObject.get("PlayerName").asText();
    }

    private String getMessageFullText(JsonNode jsonObject) throws FileNotFoundException, IOException, CsvException {
        String time = getMessageTime(jsonObject);
        String scores = getMessageScores(jsonObject);

        String text = getMessageText(jsonObject);

        return time + " " + scores + "\n" + text + "\n" + SOURCE_TEXT;
    }

    private String getMessageTime(JsonNode jsonObject) {
        String period = jsonObject.get("Period").asText();
        String gameClock = jsonObject.get("GameClock").asText();

        return "Q" + period + " " + gameClock;
    }

    private String getMessageText(JsonNode jsonObject) {
        return jsonObject.get("Text").asText();
    }

    private String getMessageScores(JsonNode jsonObject) {
        String visitorTeamAbbreviation = jsonObject.get("VisitorTeamAbbreviation").asText();
        String visitorTeamScore = jsonObject.get("VisitorTeamScore").asText();
        String homeTeamAbbreviation = jsonObject.get("HomeTeamAbbreviation").asText();
        String homeTeamScore = jsonObject.get("HomeTeamScore").asText();

        return visitorTeamAbbreviation + " " + visitorTeamScore + " · " + homeTeamAbbreviation + " " + homeTeamScore;
    }

    private List<String> getSecondaryImages(JsonNode jsonObject) {
        List<String> imageNames = imagesRepository.getImageNames();
        String personName = StringUtils.stripAccents(getPersonName(jsonObject));
        String messageText = StringUtils.stripAccents(getMessageText(jsonObject));

        return imageNames.stream().filter(imageName -> {
            String pattern = ".*\\b" + StringUtils.stripAccents(imageName) + "\\b.*";
            return messageText.matches(pattern) && !personName.matches(pattern);
        }).collect(Collectors.toList());
    }

}

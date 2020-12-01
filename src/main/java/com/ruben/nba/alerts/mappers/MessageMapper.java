package com.ruben.nba.alerts.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import com.ruben.nba.alerts.bootstrap.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    private final static String SOURCE_TEXT = "source: NBA alerts";

    public Message map (JsonNode jsonObject) {
        Message message = new Message();

        message.setMessageId(getAlertId(jsonObject));
        message.setPersonId(getPersonId(jsonObject));
        message.setText(getMessageFullText(jsonObject));

        return message;
    }

    private String getAlertId(JsonNode jsonObject) {
        return jsonObject.get("AlertId").asText();
    }

    private String getPersonId(JsonNode jsonObject) {
        return jsonObject.get("PersonId").asText();
    }

    private String getMessageFullText(JsonNode jsonObject) {
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

        return visitorTeamAbbreviation + " " + visitorTeamScore + " · " +  homeTeamAbbreviation + " " + homeTeamScore;
    }
}

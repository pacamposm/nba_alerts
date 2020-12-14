package com.ruben.nba.alerts.bootstrap;

public class Message {

    private String messageId;
    private String personId;
    private String teamId;
    private String text;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", personId='" + personId + '\'' +
                ", teamId='" + teamId + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

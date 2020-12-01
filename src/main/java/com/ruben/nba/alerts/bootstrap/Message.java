package com.ruben.nba.alerts.bootstrap;

public class Message {

    private String messageId;
    private String personId;
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

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", personId='" + personId + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

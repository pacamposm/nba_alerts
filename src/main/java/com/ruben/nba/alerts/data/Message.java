package com.ruben.nba.alerts.data;

import java.util.List;
import java.util.Objects;

public class Message {

    private String messageId;
    private String personId;
    private String teamId;
    private String text;
    private String personName;
    private List<String> secondaryImageName;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

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

    public List<String> getSecondaryImages() {
        return secondaryImageName;
    }

    public void setSecondaryImage(List<String> secondaryImageName) {
        this.secondaryImageName = secondaryImageName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, personId, personName, secondaryImageName, teamId, text);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Message other = (Message) obj;
        return Objects.equals(messageId, other.messageId) && Objects.equals(personId, other.personId)
                && Objects.equals(personName, other.personName)
                && Objects.equals(secondaryImageName, other.secondaryImageName) && Objects.equals(teamId, other.teamId)
                && Objects.equals(text, other.text);
    }

    @Override
    public String toString() {
        return "Message [messageId=" + messageId + ", personId=" + personId + ", teamId=" + teamId + ", text=" + text
                + ", personName=" + personName + ", secondaryImageName=" + secondaryImageName + "]";
    }

       

}

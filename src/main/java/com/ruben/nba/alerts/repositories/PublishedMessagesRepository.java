package com.ruben.nba.alerts.repositories;

import org.springframework.stereotype.Component;

import com.ruben.nba.alerts.data.Message;

import java.util.ArrayList;
import java.util.List;

@Component
public class PublishedMessagesRepository {

    private List<Message> publishedIds = new ArrayList<>();

    public void add(Message message) {
        publishedIds.add(message);
    }

    public void addAll(List<Message> messages) {
        publishedIds.addAll(messages);
    }

    public Boolean isPublished(Message message) {
        return publishedIds.contains(message);
    }

}

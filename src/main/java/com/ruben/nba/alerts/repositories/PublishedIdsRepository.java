package com.ruben.nba.alerts.repositories;

import com.ruben.nba.alerts.bootstrap.Message;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PublishedIdsRepository {

    private List<Message> publishedIds = new ArrayList<>();

    public void add(Message message) {
        publishedIds.add(message);
    }

    public Boolean isPublished(Message message) {
        return publishedIds.contains(message);
    }

}

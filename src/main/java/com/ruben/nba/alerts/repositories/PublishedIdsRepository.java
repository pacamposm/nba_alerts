package com.ruben.nba.alerts.repositories;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PublishedIdsRepository {

    private List<String> publishedIds = new ArrayList<>();

    public void add(String messageId) {
        publishedIds.add(messageId);
    }

    public Boolean isPublished(String messageId) {
        return publishedIds.contains(messageId);
    }

}

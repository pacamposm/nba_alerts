package com.ruben.nba.alerts.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.ruben.nba.alerts.bootstrap.Message;
import com.ruben.nba.alerts.repositories.PublishedIdsRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class AlertsFilter {

    private PublishedIdsRepository publishedIdsRepository;

    public AlertsFilter(PublishedIdsRepository publishedIdsRepository) {
        this.publishedIdsRepository = publishedIdsRepository;
    }

    public Predicate<Message> getFilter(){
        List<Predicate<Message>> allFilters = new ArrayList<>();
        allFilters.add(getIsNotPublishedFilter());

        return allFilters.stream().reduce(x->true, Predicate::and);
    }

    private Predicate<Message> getIsNotPublishedFilter(){
        return message -> !publishedIdsRepository.isPublished(message);
    }

}

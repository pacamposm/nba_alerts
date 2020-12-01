package com.ruben.nba.alerts.filters;

import com.fasterxml.jackson.databind.JsonNode;
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

    public Predicate<JsonNode> getFilter(){
        List<Predicate<JsonNode>> allFilters = new ArrayList<>();

        allFilters.add(getPersonIdFilter());
        allFilters.add(getIsNotPublishedFilter());

        return allFilters.stream().reduce(x->true, Predicate::and);
    }

    private Predicate<JsonNode> getPersonIdFilter(){
        return jsonObj -> jsonObj.get("PersonId").asInt() > 0;
    }

    private Predicate<JsonNode> getIsNotPublishedFilter(){
        return jsonObj -> !publishedIdsRepository.isPublished(jsonObj.get("AlertId").asText());
    }

}

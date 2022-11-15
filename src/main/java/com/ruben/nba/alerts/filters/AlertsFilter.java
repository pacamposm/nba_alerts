package com.ruben.nba.alerts.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.ruben.nba.alerts.data.Message;
import com.ruben.nba.alerts.repositories.PublishedMessagesRepository;

@Component
public class AlertsFilter {

    private PublishedMessagesRepository publishedIdsRepository;

    public AlertsFilter(PublishedMessagesRepository publishedIdsRepository) {
        this.publishedIdsRepository = publishedIdsRepository;
    }

    public Predicate<Message> getFilter(){
        List<Predicate<Message>> allFilters = new ArrayList<>();
        allFilters.add(getIsNotPublishedFilter());
        allFilters.add(getMessagesAllowedFilter());
        

        return allFilters.stream().reduce(x->true, Predicate::and);
    }

    private Predicate<Message> getIsNotPublishedFilter(){
        return message -> !publishedIdsRepository.isPublished(message);
    }
    


    private Predicate<Message> getMessagesAllowedFilter(){
        return message -> message.getText().toLowerCase().contains("all-time") 
        		|| message.getText().toLowerCase().contains("0 career")
        		|| message.getText().toLowerCase().contains("career high")
        		|| message.getText().toLowerCase().contains("triple-double") ;
    }

}

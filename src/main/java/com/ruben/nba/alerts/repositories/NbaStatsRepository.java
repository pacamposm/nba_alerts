package com.ruben.nba.alerts.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class NbaStatsRepository {

    private RestTemplate restTemplate = new RestTemplate();

    private final static String STATS_URL = "https://stats.nba.com/js/data/widgets/nba_stat_alerts.json";

    private final static String PHOTO_URL = "https://ak-static.cms.nba.com/wp-content/uploads/headshots/nba/latest/260x190/{playerId}.png";


    public JsonNode getAll() throws IOException {
        ResponseEntity<String> response
                = restTemplate.getForEntity(STATS_URL, String.class);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readTree(response.getBody());
    }

    public byte[] getImage(String playerId) {
        String url = PHOTO_URL.replace("{playerId}", playerId);
        return restTemplate.getForObject(url, byte[].class);
    }


}

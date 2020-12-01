package com.ruben.nba.alerts;

import com.ruben.nba.alerts.repositories.NbaStatsRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NbaAlertsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NbaAlertsApplication.class, args);
    }

}

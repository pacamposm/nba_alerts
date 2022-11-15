package com.ruben.nba.alerts.repositories;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.batik.transcoder.TranscoderException;
import org.springframework.stereotype.Component;

import com.ruben.nba.alerts.service.NbaStatsService;
import com.ruben.nba.alerts.utils.Transcoder;

@Component
public class ImagesRepository {

    private final static String PLAYER_IMAGE_PATH = "images/{playerId}.png";
    private final static String TEAM_IMAGE_PATH = "images/{teamId}.png";
    private final static String PLAYER_PER_NAME_IMAGE_PATH = "images/names/";
    private final static String PLAYER_PER_NAME_IMAGE_SUFIX = ".jpg";

    private final Transcoder transcoder;
    private final List<String> imageNames;
    private final NbaStatsService nbaStatsService;

    public List<String> getImageNames() {
        return this.imageNames;
    }

    public ImagesRepository(Transcoder transcoder, NbaStatsService nbaStatsService) {
        this.transcoder = transcoder;
        this.imageNames = initImageNames();
        this.nbaStatsService = nbaStatsService;
    }

    private List<String> initImageNames() {
        List<String> names = new ArrayList<String>();

        File[] files = new File(PLAYER_PER_NAME_IMAGE_PATH)
                .listFiles((dir, name) -> name.endsWith(PLAYER_PER_NAME_IMAGE_SUFIX));

        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName();
                name = name.replace(PLAYER_PER_NAME_IMAGE_SUFIX, "");
                names.add(name);
            }
        }

        return names;
    }

    public File getPlayer(String playerId) throws IOException {
        String path = getPlayerPath(playerId);
        File player = new File(path);
        if (player.exists()) {
            return player;
        } else {
            downloadPlayerImage(playerId);
            return new File(path);
        }
    }

    private String getPlayerPath(String playerId) {
        return PLAYER_IMAGE_PATH.replace("{playerId}", playerId);
    }

    private void downloadPlayerImage(String playerId) throws IOException {
        byte[] imageBytes = nbaStatsService.getPlayerImage(playerId);
        savePlayer(playerId, imageBytes);
    }

    private void savePlayer(String playerId, byte[] imageBytes) throws IOException {
        String path = getPlayerPath(playerId);
        Files.write(Paths.get(path), imageBytes);
    }

    public File getTeam(String teamId) throws IOException, TranscoderException {
        String path = getTeamPath(teamId);
        File team = new File(path);
        if (team.exists()) {
            return team;
        } else {
            downloadTeamImage(teamId);
            return new File(path);
        }
    }

    private String getTeamPath(String teamId) {
        return TEAM_IMAGE_PATH.replace("{teamId}", teamId);
    }

    private void downloadTeamImage(String teamId) throws IOException, TranscoderException {
        String teamImageUrl = nbaStatsService.getTeamImageURL(teamId);
        saveTeam(teamId, teamImageUrl);
    }

    private void saveTeam(String teamId, String teamImageUrl) throws IOException, TranscoderException {
        String path = getTeamPath(teamId);
        transcoder.svnToPng(teamImageUrl, path);
    }

    public File getSecondPlayer(String playerName) {
        String path = PLAYER_PER_NAME_IMAGE_PATH + playerName + PLAYER_PER_NAME_IMAGE_SUFIX;
        return new File(path);
    }

}
package com.ruben.nba.alerts.repositories;

import com.ruben.nba.alerts.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ImagesRepository {

    private final static String PLAYER_IMAGE_PATH = "images/{playerId}.png";
    private final static String TEAM_IMAGE_PATH = "images/{teamId}.png";

    private final Transcoder transcoder;

    public ImagesRepository(Transcoder transcoder) {
        this.transcoder = transcoder;
    }

    public Boolean existsPlayer(String playerId) {
        File image = getPlayer(playerId);
        return image.exists();
    }

    public Boolean existsTeam(String teamId) {
        File image = getTeam(teamId);
        return image.exists();
    }

    public void savePlayer(String playerId, byte[] imageBytes) throws IOException {
        String path = getPlayerPath(playerId);
        Files.write(Paths.get(path), imageBytes);
    }

    public void saveTeam(String teamId, String teamImageUrl) throws IOException, TranscoderException {
        String path = getTeamPath(teamId);
        transcoder.svnToPng(teamImageUrl,path);
    }

    public File getPlayer(String playerId) {
        String path = getPlayerPath(playerId);
        return new File(path);
    }

    public File getTeam(String teamId) {
        String path = getTeamPath(teamId);
        return new File(path);
    }

    private String getPlayerPath(String playerId) {
        return PLAYER_IMAGE_PATH.replace("{playerId}", playerId);
    }

    private String getTeamPath(String teamId) {
        return TEAM_IMAGE_PATH.replace("{teamId}", teamId);
    }

}

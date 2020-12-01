package com.ruben.nba.alerts.repositories;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ImagesRepository {

    private final static String IMAGE_PATH = "images/{playerId}.png";

    public Boolean exists(String playerId) {
        File image = get(playerId);
        return image.exists();
    }

    public void save(String playerId, byte[] imageBytes) throws IOException {
        String path = getPath(playerId);
        Files.write(Paths.get(path), imageBytes);
    }

    public File get(String playerId) {
        String path = getPath(playerId);
        return new File(path);
    }

    private String getPath(String playerId) {
        return IMAGE_PATH.replace("{playerId}", playerId);
    }

}

package com.ruben.nba.alerts.repositories;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.batik.transcoder.TranscoderException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImagesRepositoryTest {

    private final static String PLAYER_ID = "1627736";
    private final static String PLAYER_IMAGE_PATH = "images/" + PLAYER_ID + ".png";
    
    private final static String TEAM_ID = "1610612760";
    private final static String TEAM_IMAGE_PATH = "images/\" + TEAM_ID + \".png";
    
    private final static String SECONDARY_NAME = "Bob Sura";

    @Autowired
    private ImagesRepository imagesRepository;

    @BeforeClass
    public static void deleteImageTests() {
        deletePlayer();
        deleteTeam();
    }
    
    private static void deletePlayer() {
        File player = new File(PLAYER_IMAGE_PATH);
        player.delete();
    }
    
    private static void deleteTeam() {
        File team = new File(TEAM_IMAGE_PATH);
        team.delete();
    }
    
    
    @Test
    public void getPlayerTest() throws IOException {
        //No existe y descarga
        File player = imagesRepository.getPlayer(PLAYER_ID);
        assertTrue("El jugador existe", player.exists());
        
        //Ya existe. No descarga
        player = imagesRepository.getPlayer(PLAYER_ID);
        assertTrue("El jugador existe", player.exists());
    }
    
    
    @Test
    public void getTeamTest() throws IOException, TranscoderException {
        //No existe y descarga
        File team = imagesRepository.getTeam(TEAM_ID);
        assertTrue("El jugador existe", team.exists());
        
        //Ya existe. No descarga
        team = imagesRepository.getTeam(TEAM_ID);
        assertTrue("El jugador existe", team.exists());
    }
    
    @Test
    public void getNames() throws IOException, TranscoderException {
        List<String> names = imagesRepository.getImageNames();
        assertTrue("Contiene al jugador", names.contains(SECONDARY_NAME));
    }
    
    @Test
    public void getSecondary() throws IOException, TranscoderException {
        File player = imagesRepository.getSecondPlayer(SECONDARY_NAME);
        assertTrue("El jugador existe", player.exists());
    }
    
    

}

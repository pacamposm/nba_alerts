package com.ruben.nba.alerts.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvException;
import com.ruben.nba.alerts.data.Message;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageMapperTest {

    @Autowired
    private MessageMapper messageMapper;

    private final static String STATISTIC = "{\"AlertId\":3577648,\"AlertKey\":\"-NGoRWp4Wi6Imvi_pcHM\",\"Text\":\"Anthony Davis has passed Ben McLemore for 9th on the Kings All-Time three pointers list with 385\",\"Timestamp\":1668382940000,\"GameID\":\"0022200198\",\"PersonId\":203076,\"PlayerName\":\"Anthony Davis\",\"TeamId\":1610612747,\"Period\":4,\"GameClock\":\"4:59\",\"GameDate\":\"2022-11-13T00:00:00\",\"TeamAbbreviation\":\"LAL\",\"HomeTeamAbbreviation\":\"LAL\",\"VisitorTeamAbbreviation\":\"BKN\",\"HomeTeamScore\":104,\"VisitorTeamScore\":89}";
    private final static String MESSAGE_ID = "3577648";
    private final static String PERSON_ID = "203076";
    private final static String TEAM_ID = "1610612747";
    private final static String TEXT_FRAGMENT = "Anthony Davis has passed Ben McLemore for 9th on the Kings All-Time three pointers list with 385";
    private final static String PERSON_NAME = "Anthony Davis";
    private final static ArrayList<String> SECOND_IMAGE_NAMES = new ArrayList<String>(Arrays.asList("Ben McLemore"));

    @Test
    public void mapperTest() throws IOException, CsvException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readValue(STATISTIC, JsonNode.class);
        Message message = messageMapper.map(actualObj);

        assertEquals(MESSAGE_ID, message.getMessageId());
        assertEquals(PERSON_ID, message.getPersonId());
        assertEquals(TEAM_ID, message.getTeamId());
        assertTrue(message.getText().contains(TEXT_FRAGMENT));
        assertEquals(PERSON_NAME, message.getPersonName());
        assertEquals(SECOND_IMAGE_NAMES, message.getSecondaryImages());
    }
}

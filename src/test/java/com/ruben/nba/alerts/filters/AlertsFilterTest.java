package com.ruben.nba.alerts.filters;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ruben.nba.alerts.data.Message;
import com.ruben.nba.alerts.repositories.PublishedMessagesRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlertsFilterTest {

    @Autowired
    private  AlertsFilter filter;

    @Autowired
    private PublishedMessagesRepository publishedIdsRepository;


    private static List<Message> messages = new ArrayList<>();
    private List<Message> resultMessages = new ArrayList<>();
    
    private final static String ALL_TIME_TEXT = "String containing all-time words";
    private static Message allTimeMessage = new Message();
    
    private final static String ZERO_CAREER_TEXT = "String containing 0 career words";
    private static Message zeroCareerMessage = new Message();
    
    private final static String CAREER_HIGH_TEXT = "String containing career high words";
    private static Message careerHighMessage = new Message();
    
    private final static String TRIPLE_DOUBLE_TEXT = "String containing triple-double words";
    private static Message tripleDoubleMessage = new Message();
    
    private final static String PUBLISHED_TEXT = "Second string containing all-time words";
    private static Message publishedMessage = new Message();
    
    private final static String NONCOMPLIANT_TEXT = "That string does not complies";
    private static Message noncompliantMessgae = new Message();
    
       
    
    @BeforeClass
    public static void initMessagesList() {
        allTimeMessage.setText(ALL_TIME_TEXT);
        zeroCareerMessage.setText(ZERO_CAREER_TEXT);
        careerHighMessage.setText(CAREER_HIGH_TEXT);
        tripleDoubleMessage.setText(TRIPLE_DOUBLE_TEXT);
        publishedMessage.setText(PUBLISHED_TEXT);
        noncompliantMessgae.setText(NONCOMPLIANT_TEXT);

        messages.add(allTimeMessage);
        messages.add(zeroCareerMessage);
        messages.add(careerHighMessage);
        messages.add(tripleDoubleMessage);
        messages.add(publishedMessage);
        messages.add(noncompliantMessgae);
    }
    
    
    @Before
    public void before() {
        initPublishedList();
        filterMessages();
    }
    public void initPublishedList() {
        publishedIdsRepository.add(publishedMessage);
    }
    
    private void filterMessages() {
        resultMessages = messages.stream().filter(filter.getFilter()).collect(Collectors.toList());
    }
    
    @Test
    public void filtersTest() {
        okTest();
        publishedTest();
        allowedTest();
    }
    
    public void okTest() {
        assertTrue(resultMessages.contains(allTimeMessage));
        assertTrue(resultMessages.contains(zeroCareerMessage));
        assertTrue(resultMessages.contains(careerHighMessage));
        assertTrue(resultMessages.contains(tripleDoubleMessage));
    }
    
    public void publishedTest() {
        assertFalse(resultMessages.contains(publishedMessage));        
    }
    
    public void allowedTest() {
        assertFalse(resultMessages.contains(noncompliantMessgae));
    }
    
}

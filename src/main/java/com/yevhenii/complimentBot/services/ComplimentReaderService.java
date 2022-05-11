package com.yevhenii.complimentBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class ComplimentReaderService {

    @Autowired
    private FileReaderWriterService frService;

    @Value("${complimentFilePath}")
    private String pathToComplimentFile;

    private List<String> compliments = new ArrayList<>();
    private Iterator<String> complimentIterator;

    @PostConstruct
    void init() {
        readComplimentsFromFile();
    }

    private void readComplimentsFromFile() {

        System.out.println("path to compliments file: " + new File(pathToComplimentFile).getAbsolutePath());
        compliments = frService.readLinersFromFile(pathToComplimentFile);

        Collections.shuffle(compliments);

        complimentIterator = compliments.iterator();
    }

    public String getCompliment() {
        if (!complimentIterator.hasNext()) {
            complimentIterator = compliments.iterator();
        }
        String compliment = complimentIterator.next();
        System.out.println(LocalDateTime.now() + " Compliment to send is: " + compliment);
        return "Кохана. " + compliment;
    }
}

package com.yevhenii.complimentBot.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class ComplimentReader {

    private List<String> compliments = new ArrayList<>();
    private Iterator<String> complimentIterator;
    private File complimentFile;

    @Autowired
    public ComplimentReader(File complimentFile) {
        this.complimentFile = complimentFile;
        readComplimentsFromFile();
    }

    private void readComplimentsFromFile() {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(complimentFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                compliments.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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

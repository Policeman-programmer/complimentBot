package com.yevhenii.complimentBot.services;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileReaderWriterService {

    public List<String> readLinersFromFile(String pathToFile) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
//                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public void writeLineToFile(String line, String pathToFile) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathToFile, true))) {
            bw.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

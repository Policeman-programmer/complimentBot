package com.yevhenii.complimentBot;

import com.yevhenii.complimentBot.utils.ComplimentReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.ApiContextInitializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ComplimentBotApplicationTests {

    @BeforeAll
    static void loadBotContext() {
        ApiContextInitializer.init();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testRefreshComplimentsList() throws IOException {
        File complimentFile = File.createTempFile("tempComplimentFile", ".txt");
        FileWriter fileWriter = new FileWriter(complimentFile, true);

        String compl1 = "compliment 1";

        fileWriter.append(compl1).append("\n");

        fileWriter.flush();
        fileWriter.close();

        ComplimentReader complimentReader = new ComplimentReader(complimentFile);

        assertThat(complimentReader.getCompliment()).isEqualTo("Кохана. " + compl1);

    }

}

package com.yevhenii.complimentBot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;

import static com.yevhenii.complimentBot.Constants.*;

@Service
public class SchedulerService {

    private Thread thread;

    private int maxTimeToNextCompliment = HOURS_6_MS;

    long timeForNextCompliment;

    Random random = new Random();

    public void startRandomScheduler(String chatId, Consumer<String> sendMessage) {
        System.out.println("RandomScheduler was started");
        if (thread == null) {
            thread = new Thread(() -> {

                timeForNextCompliment = System.currentTimeMillis();

                while (true) {
                    if (LocalTime.now(KYIV_ZONE_ID).isAfter(BEGIN_OF_DAY) && LocalTime.now(KYIV_ZONE_ID).isBefore(END_OF_DAY)) {

                        if (System.currentTimeMillis() > timeForNextCompliment) {
                            sendMessage.accept(chatId);
                            calculateTimeForNextCompliment();
                        }
                    }
                }
            });
            thread.start();
        }
    }

    private void calculateTimeForNextCompliment() {
        int delay = random.nextInt(maxTimeToNextCompliment);
        System.out.println("delay is: " + delay / 1000 / 60 + " minutes");
        timeForNextCompliment = System.currentTimeMillis() + delay;
        System.out.println("Next compliment will be at: " + new Date(timeForNextCompliment));
    }

    public void updateMaxTimeToNextCompliment(int maxTimeToNextCompliment) {
        this.maxTimeToNextCompliment = maxTimeToNextCompliment;
        calculateTimeForNextCompliment();
    }

    public int getMaxTimeToNextCompliment() {
        return maxTimeToNextCompliment;
    }
}

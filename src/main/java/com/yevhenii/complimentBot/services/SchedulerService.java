package com.yevhenii.complimentBot.services;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Random;
import java.util.function.Consumer;

import static com.yevhenii.complimentBot.Constants.*;

@Service
public class SchedulerService {

    private Thread thread;
    private int delay;

    public void startRandomScheduler(Long chatId, Consumer<Long> sendMessage) {
        System.out.println("RandomScheduler was started");
        if (thread == null) {
            thread = new Thread(() -> {

                Random random = new Random();

                int maxTimeToNextCompliment = 21_600_000; //6 hour
                long timeForNextCompliment = System.currentTimeMillis();

                while (true) {

                    if (LocalTime.now(KYIV_ZONE_ID).isAfter(BEGIN_OF_DAY) && LocalTime.now(KYIV_ZONE_ID).isBefore(END_OF_DAY)) {

                        if (System.currentTimeMillis() > timeForNextCompliment) {

                            sendMessage.accept(chatId);

                            delay = random.nextInt(maxTimeToNextCompliment);
                            System.out.println("delay is: " + delay / 1000 / 60 + " minutes");
                            timeForNextCompliment = System.currentTimeMillis() + delay;
                        }

                    }
                }
            });
            thread.start();
        }
    }

}

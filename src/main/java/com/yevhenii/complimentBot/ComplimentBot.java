package com.yevhenii.complimentBot;

import com.yevhenii.complimentBot.utils.ComplimentReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.*;

import static com.yevhenii.complimentBot.utils.Constants.*;


@Component
public class ComplimentBot extends TelegramLongPollingBot {

    @Autowired
    private ComplimentReader complimentReader;

    @Value("${name}")
    private String name;

    @Value("${token}")
    private String token;

    Thread thread;
    long delay;


    ComplimentBot(TelegramBotsApi botsApi) throws TelegramApiRequestException {
        botsApi.registerBot(this);
    }

    @PostConstruct
    private void init() {
        Long chatId = 302864380L; //toDo: avoid hardcode
        startRandomScheduler(chatId); // after firs interaction with bot it remembers chat id in order to after
                                        //restart app sheduller will continue to work
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();

            String msgTest = update.getMessage().getText();
            if (COMPLIMENT_AGAIN.equals(msgTest)) {
                createSendCompliment(chatId);
            }else if(START.equals(msgTest)){
                createSendCompliment(chatId);
                startRandomScheduler(chatId); // user need it for the first interaction with bot
            }
        }

    }

    private void startRandomScheduler(Long chatId) {
        System.out.println("RandomScheduler was started");
        if (thread == null) {
            thread = new Thread(() -> {

                Random random = new Random();

                int maxTimeToNextCompliment = 21_600_000; //6 hour
                long timeForNextCompliment = System.currentTimeMillis();

                while (true) {

                    if (LocalTime.now(KYIV_ZONE_ID).isAfter(BEGIN_OF_DAY) && LocalTime.now(KYIV_ZONE_ID).isBefore(END_OF_DAY)) {

                        if (System.currentTimeMillis() > timeForNextCompliment) {
                            createSendCompliment(chatId);

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


    private void createSendCompliment(Long chatId) {
        SendMessage message = createMessage(chatId);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private SendMessage createMessage(long chatId) {
        return new SendMessage()
                .setChatId(chatId)
                .setText(complimentReader.getCompliment())
                .setReplyMarkup(createReplyKeyboard());
    }

    public synchronized ReplyKeyboardMarkup createReplyKeyboard() {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton(COMPLIMENT_AGAIN));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        // и устанваливаем этот список нашей клавиатуре
        return replyKeyboardMarkup.setKeyboard(keyboard);
    }

    @Override
    public String getBotUsername() {
        return "Trokhniuk_bot";
    }

    @Override
    public String getBotToken() {
        return "5354671221:AAEKJn--hBdCo5fhcrAsojuh2K_4h9cowss";
    }
}

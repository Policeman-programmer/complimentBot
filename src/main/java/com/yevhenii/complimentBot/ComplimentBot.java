package com.yevhenii.complimentBot;

import com.yevhenii.complimentBot.services.ComplimentReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.yevhenii.complimentBot.Constants.*;


@Component
public class ComplimentBot extends TelegramLongPollingBot {

    @Autowired
    private ComplimentReaderService complimentReader;

//    @Value("${botName}")
//    private String name;

//    @Value("${botToken}")
//    private String token;

    private Thread thread;

    private long delay;

    ComplimentBot(TelegramBotsApi botsApi) throws TelegramApiRequestException {
        botsApi.registerBot(this);
    }

    @PostConstruct
    private void init() {
        startRandomScheduler(DI_CHAT_ID); //it needed in order restart app scheduler will continue to work
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            Message message = update.getMessage();
            Chat chat = message.getChat();
            Long chatId = message.getChatId();

            if (MY_USER_NAME.equals(chat.getUserName())) {

                if (REGISTER.equals(message.getText())) {
                    try {
                        chatId = chat.getId();
                        execute(new SendMessage()
                                .setChatId(chatId)
                                .setText(chatId.toString()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

            } else if (DI_USER_NAME.equals(chat.getUserName())) { //any message Diana send me redirect to me :)

                String msgTest = message.getText();

                if (COMPLIMENT_AGAIN.equals(msgTest)) {

                    createSendCompliment(chatId);

                } else {

                    try {
                        execute(new SendMessage()
                                .setChatId(DI_CHAT_ID)
                                .setText("Я розумію лише певні команди, тож я передам це послання вашому коханому"));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    try {
                        execute(new SendMessage()
                                .setChatId(MY_CHAT_ID)
                                .setText("Послання від Коханої " + msgTest));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                }

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
        return "Trokhniuk_bot"; // TestCharmyBot
    }
    @Override
    public String getBotToken() {
        return "5354671221:AAEKJn--hBdCo5fhcrAsojuh2K_4h9cowss"; // 5190160613:AAEptI8l0_IJqsH-M6c_38rqFXzxCgKAPQE
    }
}

package com.yevhenii.complimentBot;

import com.yevhenii.complimentBot.services.BotKeyboardService;
import com.yevhenii.complimentBot.services.ComplimentReaderService;
import com.yevhenii.complimentBot.services.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

import static com.yevhenii.complimentBot.Constants.*;

@Component
public class ComplimentBot extends TelegramLongPollingBot {

    @Autowired
    private ComplimentReaderService complimentReader;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private BotKeyboardService botKeyboardService;

//    @Value("${botName}")
//    private String name;
//
//    @Value("${botToken}")
//    private String token;

    @PostConstruct
    private void init() {
        schedulerService.startRandomScheduler(DI_CHAT_ID, this::createAndSendComplimentByChatId); //it needed in order restart app scheduler will continue to work
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

                    createAndSendComplimentByChatId(chatId);

                } else {

                    replyToOtherTextAndSendItToBotOwner(msgTest);
                }
            }
        }
    }

    private void replyToOtherTextAndSendItToBotOwner(String msgTest) {
        try {
            execute(new SendMessage()
                    .setChatId(DI_CHAT_ID)
                    .setText(OTHER_TEXT_REPLY));
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

    private void createAndSendComplimentByChatId(Long chatId) {

        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(complimentReader.getCompliment())
                .setReplyMarkup(botKeyboardService.createReplyKeyboard());

        try {
            execute(sendMessage); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "TestCharmyBot"; //Trokhniuk_bot
    }

    @Override
    public String getBotToken() {
        return "5190160613:AAEptI8l0_IJqsH-M6c_38rqFXzxCgKAPQE"; //5354671221:AAEKJn--hBdCo5fhcrAsojuh2K_4h9cowss
    }
}

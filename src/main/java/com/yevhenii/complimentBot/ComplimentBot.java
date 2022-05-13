package com.yevhenii.complimentBot;

import com.yevhenii.complimentBot.services.BotKeyboardService;
import com.yevhenii.complimentBot.services.ComplimentReaderService;
import com.yevhenii.complimentBot.services.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    ComplimentBot(TelegramBotsApi botsApi) throws TelegramApiRequestException {
        botsApi.registerBot(this);
    }

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

            String msgTest = message.getText();

            checkIfSetInterval(msgTest, chatId);

            if (COMPLIMENT_AGAIN.equals(msgTest)) {

                createAndSendComplimentByChatId(chatId);

            } else if (CHANGE_COMPLIMENT_INTERVAL.equals(msgTest)) {
                String str = "Максимальний інтервал між компліментами буже складати";
                intervalChangedReply(chatId, str, botKeyboardService.createIntervalsKeyboard());
            } else {
                replyToOtherTextAndSendItToBotOwner(message);
            }
        }
    }

    private void checkIfSetInterval(String msgTest, Long chatId) {
        if (DAY.equals(msgTest))
            schedulerService.updateMaxTimeToNextCompliment(DAY_MILLS);
        if (HOURS_12.equals(msgTest))
            schedulerService.updateMaxTimeToNextCompliment(HOURS_12_MS);
        if (HOURS_6.equals(msgTest))
            schedulerService.updateMaxTimeToNextCompliment(HOURS_6_MS);
        if (HOURS_3.equals(msgTest))
            schedulerService.updateMaxTimeToNextCompliment(HOURS_3_MS);
        if (DAY.equals(msgTest) || HOURS_12.equals(msgTest) || HOURS_6.equals(msgTest) || HOURS_3.equals(msgTest)) {
            intervalChangedReply(chatId, NEXT_COMPLIMENT_TIME_NOTIFICATION + msgTest,
                    botKeyboardService.createReplyKeyboard());
        }
    }

    private void intervalChangedReply(Long chatId, String message, ReplyKeyboardMarkup replyKeyboard) {
        try {
            execute(new SendMessage()
                    .setChatId(chatId)
                    .setText(message)
                    .setReplyMarkup(replyKeyboard));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void replyToOtherTextAndSendItToBotOwner(Message msg) {
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
                    .setText("Послання від Коханої:\n" + msg.getText()));
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
        return "CharmyTest_bot"; //Trokhniuk_bot
    }

    @Override
    public String getBotToken() {
        return "5190160613:AAEptI8l0_IJqsH-M6c_38rqFXzxCgKAPQE"; //5354671221:AAEKJn--hBdCo5fhcrAsojuh2K_4h9cowss
    }
}

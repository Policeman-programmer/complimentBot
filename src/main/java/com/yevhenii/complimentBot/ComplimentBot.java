package com.yevhenii.complimentBot;

import com.yevhenii.complimentBot.services.BotKeyboardService;
import com.yevhenii.complimentBot.services.ComplimentReaderService;
import com.yevhenii.complimentBot.services.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

    @Autowired
    ComplimentBot(TelegramBotsApi botsApi) throws TelegramApiException {
        botsApi.registerBot(this);
    }

    @PostConstruct
    private void init() {
        schedulerService.startRandomScheduler(DI_CHAT_ID, this::createAndSendComplimentByChatId); //it needed in order restart app scheduler will continue to work
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {

                Message message = update.getMessage();
                String chatId = message.getChatId().toString();
                String msgTest = message.getText();

                switch (msgTest){
                    case DAY:
                        schedulerService.updateMaxTimeToNextCompliment(DAY_MILLS);
                        sendReplyAboutIntervalChanging(chatId, msgTest);
                        break;
                    case HOURS_12:
                        schedulerService.updateMaxTimeToNextCompliment(HOURS_12_MS);
                        sendReplyAboutIntervalChanging(chatId, msgTest);
                        break;
                    case HOURS_6:
                        schedulerService.updateMaxTimeToNextCompliment(HOURS_6_MS);
                        sendReplyAboutIntervalChanging(chatId, msgTest);
                        break;
                    case HOURS_3:
                        schedulerService.updateMaxTimeToNextCompliment(HOURS_3_MS);
                        sendReplyAboutIntervalChanging(chatId, msgTest);
                        break;
                    case REGISTER:
                        execute(SendMessage.builder().text(chatId).chatId(chatId).build());
                        break;
                    case COMPLIMENT_AGAIN:
                        createAndSendComplimentByChatId(chatId);
                        break;
                    case CHANGE_COMPLIMENT_INTERVAL:
                        execute(SendMessage
                                .builder()
                                .chatId(chatId)
                                .text("Максимальний інтервал між компліментами буде складати:")
                                .replyMarkup(botKeyboardService.createIntervalsKeyboard())
                                .build());
                        break;
                    default:
                        replyToOtherTextAndSendItToBotOwner(message);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendReplyAboutIntervalChanging(String chatId, String msgTest) throws TelegramApiException {
        execute(SendMessage
                .builder()
                .chatId(chatId)
                .text(NEXT_COMPLIMENT_TIME_NOTIFICATION + msgTest)
                .replyMarkup(botKeyboardService.createReplyKeyboard())
                .build());
    }

    private void replyToOtherTextAndSendItToBotOwner(Message msg) throws TelegramApiException {
        execute(SendMessage
                .builder()
                .chatId(DI_CHAT_ID)
                .text(OTHER_TEXT_REPLY)
                .build());
        execute(SendMessage
                .builder()
                .chatId(MY_CHAT_ID)
                .text("Послання від Коханої:\n" + msg.getText())
                .build());
    }

    private void createAndSendComplimentByChatId(String chatId) {
        try {
            execute(SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(complimentReader.getCompliment())
                    .replyMarkup(botKeyboardService.createReplyKeyboard())
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "Trokhniuk_bot"; // CharmyTest_bot
    }

    @Override
    public String getBotToken() {
        return "5354671221:AAEKJn--hBdCo5fhcrAsojuh2K_4h9cowss"; //5190160613:AAEptI8l0_IJqsH-M6c_38rqFXzxCgKAPQE
    }
}

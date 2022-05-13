package com.yevhenii.complimentBot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.yevhenii.complimentBot.Constants.*;

@Service
public class BotKeyboardService {

    ReplyKeyboardMarkup replyKeyboardMarkup;

    public BotKeyboardService(){
        // Create keyboard
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
    }

    public synchronized ReplyKeyboardMarkup createReplyKeyboard() {
        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton(COMPLIMENT_AGAIN));
        keyboardFirstRow.add(new KeyboardButton(CHANGE_COMPLIMENT_INTERVAL));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        // и устанваливаем этот список нашей клавиатуре
        return replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public synchronized ReplyKeyboardMarkup createIntervalsKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(DAY));
        keyboardFirstRow.add(new KeyboardButton(HOURS_12));
        keyboardFirstRow.add(new KeyboardButton(HOURS_6));
        keyboardFirstRow.add(new KeyboardButton(HOURS_3));
        keyboard.add(keyboardFirstRow);
        return replyKeyboardMarkup.setKeyboard(keyboard);
    }

}

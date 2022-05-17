package com.yevhenii.complimentBot;

import java.time.LocalTime;
import java.time.ZoneId;

public interface Constants {

    String COMPLIMENT_AGAIN = "А можна ще компліментик? :)";

    String START = "/start";

    ZoneId KYIV_ZONE_ID = ZoneId.of("Europe/Kiev");

    LocalTime BEGIN_OF_DAY = LocalTime.of(8,0);

    LocalTime END_OF_DAY = LocalTime.of(22,0);

    String MY_USER_NAME = "Yevhenii_Tr";

    String DI_USER_NAME = "TrDiana";

    String DI_CHAT_ID = "302864380";

    String MY_CHAT_ID = "272231277";

    String REGISTER = "/register"; //register as bot owner. When compliment receiver will send the reply Bot Owner will receive this reply

    String OTHER_TEXT_REPLY = "Я розумію лише певні команди, тож я передам це послання вашому коханому";

    String CHANGE_COMPLIMENT_INTERVAL = "Хочу змінити інтервал отримання компліментів";

    String DAY = "добу";

    String HOURS_12 = "12 годин";

    String HOURS_6 = "6 годин";

    String HOURS_3 = "3 години";

    int DAY_MILLS = 86_400_000;

    int HOURS_12_MS = 43_200_000;

    int HOURS_6_MS = 21_600_000;

    int HOURS_3_MS = 10_800_000;

    String NEXT_COMPLIMENT_TIME_NOTIFICATION = "Інтервал змінено. Наступний комплімент буде не пізніше ніж через ";



}

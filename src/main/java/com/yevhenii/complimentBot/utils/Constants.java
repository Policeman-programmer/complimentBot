package com.yevhenii.complimentBot.utils;

import java.time.LocalTime;
import java.time.ZoneId;

public interface Constants {

    String COMPLIMENT_AGAIN = "А можна ще комліментик? :)";

    String START = "/start";

    ZoneId KYIV_ZONE_ID = ZoneId.of("Europe/Kiev");

    LocalTime BEGIN_OF_DAY = LocalTime.of(8,0);

    LocalTime END_OF_DAY = LocalTime.of(22,0);

}

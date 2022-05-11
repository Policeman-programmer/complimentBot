package com.yevhenii.complimentBot;

import java.time.LocalTime;
import java.time.ZoneId;

public interface Constants {

    String COMPLIMENT_AGAIN = "А можна ще комліментик? :)";

    String START = "/start";

    ZoneId KYIV_ZONE_ID = ZoneId.of("Europe/Kiev");

    LocalTime BEGIN_OF_DAY = LocalTime.of(8,0);

    LocalTime END_OF_DAY = LocalTime.of(22,0);

    String MY_USER_NAME = "Yevhenii_Tr";

    String DI_USER_NAME = "TrDiana";

    Long DI_CHAT_ID = 302864380L;

    Long MY_CHAT_ID = null;//272231277

    String REGISTER = "/register"; //register as bot owner. When compliment receiver will send the reply Bot Owner will receive this reply

}

package com.ufps.cryptobot.mapper;

import com.ufps.cryptobot.contract.Message;
import com.ufps.cryptobot.contract.NewsMessage;
import org.springframework.stereotype.Component;

@Component
public class NewsMapper {

    private final String moneyBagEmoji = "\uD83D\uDD14";
    private final String flagIndicatorEmoji = "\uD83D\uDCCD";

    public NewsMapper() {
    }

    public Message NewsMessageToMessage(NewsMessage newsMessage) {
        String title = moneyBagEmoji + newsMessage.getTitle();
        String link = flagIndicatorEmoji + newsMessage.getLink();
        String text = title + "\n" + link;

        return new Message(newsMessage.getChatID(), text);
    }
}

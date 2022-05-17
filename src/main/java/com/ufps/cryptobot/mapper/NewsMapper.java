package com.ufps.cryptobot.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufps.cryptobot.contract.Event;
import com.ufps.cryptobot.contract.Message;
import com.ufps.cryptobot.contract.NewsMessage;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class NewsMapper {

    private final String moneyBagEmoji = "\uD83D\uDD14";
    private final String flagIndicatorEmoji = "\uD83D\uDCCD";

    public NewsMapper() {
    }

    public Message NewsMessageEventToMessage(Event newsMessageEvent) throws JsonProcessingException {

        byte[] decodedBytes = Base64.getDecoder().decode(newsMessageEvent.getData());

        String decodedString = new String(decodedBytes);

        NewsMessage newsMessage = new ObjectMapper().readValue(decodedString, NewsMessage.class);


        String title = moneyBagEmoji + newsMessage.getTitle();
        String link = flagIndicatorEmoji + newsMessage.getLink();
        String text = title + "\n" + link;

        return new Message(newsMessage.getChatID(), text);
    }
}

package com.ufps.cryptobot.controller.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufps.cryptobot.controller.rest.contract.AccountEvent;
import com.ufps.cryptobot.controller.rest.contract.SendMessage;
import com.ufps.cryptobot.provider.telegram.contract.Message;
import com.ufps.cryptobot.provider.pubsub.contract.Event;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class PubSubEventMapper {

    public PubSubEventMapper() {
    }

    public Message pubSubEventToTelegramMessage(Event pubSubEvent) throws JsonProcessingException {
        byte[] decodedBytes = Base64.getDecoder().decode(pubSubEvent.getData());

        String decodedString = new String(decodedBytes);

        SendMessage sendMessage = new ObjectMapper().readValue(decodedString, SendMessage.class);

        return new Message(sendMessage.getChatID(), sendMessage.getMessage());
    }

    public AccountEvent pubSubEventToAccountEvent(Event pubSubEvent) throws JsonProcessingException {
        byte[] decodedBytes = Base64.getDecoder().decode(pubSubEvent.getData());

        String decodedString = new String(decodedBytes);

        return new ObjectMapper().readValue(decodedString, AccountEvent.class);
    }
}

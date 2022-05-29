package com.ufps.cryptobot.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufps.cryptobot.contract.Event;
import com.ufps.cryptobot.contract.Message;
import com.ufps.cryptobot.contract.exchange.Crypto;
import com.ufps.cryptobot.contract.exchange.ExchangeMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Base64;

@Component
public class ExchangeMapper {

    public ExchangeMapper() {
    }

    public Message ExchangeMessageEventToMessage(Event exchangeMessageEvent) throws JsonProcessingException {
        byte[] decodedBytes = Base64.getDecoder().decode(exchangeMessageEvent.getData());

        String decodedString = new String(decodedBytes);

        ExchangeMessage exchangeMessage = new ObjectMapper().readValue(decodedString, ExchangeMessage.class);

        String msg = "";

        if (exchangeMessage.getCryptos().size() > 1) {
            for (Crypto c : exchangeMessage.getCryptos()) {
                String line = c.getName() + " " + c.getHistory().get(0) + " USD";
                msg = msg + line + "\n";
            }
        } else {
            msg = msg + "Crypto: " + exchangeMessage.getCryptos().get(0).getName() + "\n";
            msg = msg + "Currency: USD" + "\n";
            msg = msg + "History" + "\n";
            LocalDate now = LocalDate.now();
            int i = 0;
            for (Double value : exchangeMessage.getCryptos().get(0).getHistory()) {
                msg = msg + now.minusDays(i) + " " + value + "\n";
                i++;
            }
        }


        return new Message(exchangeMessage.getChatID(), msg);
    }
}

package com.ufps.cryptobot.service;

import com.ufps.cryptobot.contract.Message;
import com.ufps.cryptobot.contract.Update;
import com.ufps.cryptobot.telegram.Provider;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {
    private Provider provider;

    public MessagingService(Provider provider) {
        this.provider = provider;
    }

    public void pushMessageToUser(Message message) {
        this.provider.sendMessage(message);
    }

    public void pushUnrecognizedCommand(Update update) {
        String unrecognizedCommandMessage = "Comando desconocido. ¿Qué dijiste?";
        update.getMessage().setText(unrecognizedCommandMessage);
        this.provider.sendMessage(update.getMessage());
    }
}

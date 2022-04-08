package com.ufps.cryptobot.service;

import com.ufps.cryptobot.contract.Update;
import com.ufps.cryptobot.telegram.Provider;
import org.springframework.stereotype.Service;

@Service
public class BadRequestService {
    private Provider provider;

    public BadRequestService(Provider provider) {
        this.provider = provider;
    }

    public void pushUnrecognizedCommand(Update update) {
        String unrecognizedCommandMessage = "Comando desconocido. ¿Qué dijiste?";
        update.getMessage().setText(unrecognizedCommandMessage);
        this.provider.sendMessage(update.getMessage());
    }
}

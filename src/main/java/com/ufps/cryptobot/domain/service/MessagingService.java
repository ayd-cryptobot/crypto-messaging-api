package com.ufps.cryptobot.domain.service;

import com.ufps.cryptobot.provider.telegram.contract.Message;
import com.ufps.cryptobot.provider.telegram.contract.Update;
import com.ufps.cryptobot.controller.MessagingServiceI;
import org.springframework.stereotype.Service;

@Service
public class MessagingService implements MessagingServiceI {
    private MessagingProviderI provider;

    private final String[][] replyHomeKeyboard = {
            {"Gestionar mis crypto","Gestionar mi perfil"},
            {"Consultar precio histórico de una crypto","Consultar noticias de una crypto"}
    };

    public MessagingService(MessagingProviderI provider) {
        this.provider = provider;
    }

    public void sendMessageToUser(Message message) {
        this.provider.sendMessage(message, null);
    }

    public void sendHomeKeyboard(Update update) {
        String invitationMessage = "¡Hola " + update.getMessage().getFrom().getFirst_name() + "! Selecciona una opción del teclado";
        update.getMessage().setText(invitationMessage);
        this.provider.sendMessage(update.getMessage(), replyHomeKeyboard);
    }
}

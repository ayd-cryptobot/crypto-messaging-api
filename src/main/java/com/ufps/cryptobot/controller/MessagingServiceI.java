package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.controller.rest.contract.DiffuseMessage;
import com.ufps.cryptobot.provider.telegram.contract.Message;

public interface MessagingServiceI {
    void sendMessageToUser(Message message);

    void sendHomeKeyboard(Message message);

    void sendCryptosKeyboard(Message message);

    void sendLoginInlineKeyboard(Message message, String text);

    void diffuseMessage(DiffuseMessage diffuseMessage);
}

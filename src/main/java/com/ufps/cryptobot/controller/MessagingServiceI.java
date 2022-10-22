package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.provider.telegram.contract.Message;
import com.ufps.cryptobot.provider.telegram.contract.Update;

public interface MessagingServiceI {
    void sendMessageToUser(Message message);
    void sendHomeKeyboard(Update update);
}

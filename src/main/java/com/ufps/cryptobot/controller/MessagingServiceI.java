package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.contract.Message;
import com.ufps.cryptobot.contract.Update;

public interface MessagingServiceI {
    void pushMessageToUser(Message message);
    void pushUnrecognizedCommand(Update update);
}

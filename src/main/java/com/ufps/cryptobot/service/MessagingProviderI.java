package com.ufps.cryptobot.service;

import com.pengrad.telegrambot.response.SendResponse;
import com.ufps.cryptobot.contract.Message;

public interface MessagingProviderI {
    SendResponse sendMessage(Message message);
}

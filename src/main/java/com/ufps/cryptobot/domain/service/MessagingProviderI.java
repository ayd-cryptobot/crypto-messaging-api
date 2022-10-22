package com.ufps.cryptobot.domain.service;

import com.pengrad.telegrambot.response.SendResponse;
import com.ufps.cryptobot.provider.telegram.contract.Message;

public interface MessagingProviderI {
    SendResponse sendMessage(Message message, String[][] keyboard);
}

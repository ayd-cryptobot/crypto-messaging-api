package com.ufps.cryptobot.domain.service.messaging;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.response.SendResponse;
import com.ufps.cryptobot.provider.telegram.contract.ImageMessage;
import com.ufps.cryptobot.provider.telegram.contract.Message;

public interface MessagingProviderI {
    SendResponse sendMessage(Message message, Keyboard keyboard);

    SendResponse sendImageMessage(ImageMessage imageMessage);
}

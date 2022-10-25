package com.ufps.cryptobot.domain.service.exchange;

import com.ufps.cryptobot.provider.telegram.contract.Message;

public interface MessagingServiceI {
    void sendMessageToUser(Message message);
}

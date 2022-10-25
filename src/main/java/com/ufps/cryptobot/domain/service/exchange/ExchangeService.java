package com.ufps.cryptobot.domain.service.exchange;

import com.ufps.cryptobot.controller.ExchangeServiceI;
import com.ufps.cryptobot.provider.telegram.contract.Message;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService implements ExchangeServiceI {

    MessagingServiceI messagingService;
    HTTPClientI httpClient;

    public ExchangeService(MessagingServiceI messagingService, HTTPClientI httpClient) {
        this.messagingService = messagingService;
        this.httpClient = httpClient;
    }

    @Override
    public void cryptoHistoricalPrice(Message message, String crypto) {
        //TODO calculate query dates (initial date - final date)
        //TODO HTTP query prices
        this.messagingService.sendMessageToUser(message);
    }
}

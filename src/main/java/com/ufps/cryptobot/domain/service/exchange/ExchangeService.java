package com.ufps.cryptobot.domain.service.exchange;

import com.ufps.cryptobot.controller.ExchangeServiceI;
import com.ufps.cryptobot.domain.rest.contract.QueryHistoricalPrice;
import com.ufps.cryptobot.domain.service.messaging.MessagingProviderI;
import com.ufps.cryptobot.provider.telegram.contract.Message;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
public class ExchangeService implements ExchangeServiceI {

    private MessagingProviderI messagingProvider;
    private ExchangeHTTPRequesterI exchangeHTTPRequester;

    public ExchangeService(MessagingProviderI messagingProvider, ExchangeHTTPRequesterI exchangeHTTPRequester) {
        this.messagingProvider = messagingProvider;
        this.exchangeHTTPRequester = exchangeHTTPRequester;
    }

    @Override
    public void cryptoHistoricalPrice(Message message, String crypto) {
        //TODO calculate query dates (initial date - final date)

        //TODO HTTP query prices
        QueryHistoricalPrice queryHistoricalPrice = new QueryHistoricalPrice(crypto);
        this.exchangeHTTPRequester.queryHistoricalPrice(queryHistoricalPrice);

        this.messagingProvider.sendMessage(message, null);
    }
}

package com.ufps.cryptobot.domain.service.exchange;

import com.ufps.cryptobot.controller.ExchangeServiceI;
import com.ufps.cryptobot.domain.rest.contract.response.CryptoPrice;
import com.ufps.cryptobot.domain.rest.contract.response.HistoricalPriceResponse;
import com.ufps.cryptobot.domain.rest.contract.query.QueryHistoricalPrice;
import com.ufps.cryptobot.domain.service.messaging.MessagingProviderI;
import com.ufps.cryptobot.provider.telegram.contract.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ExchangeService implements ExchangeServiceI {

    private final MessagingProviderI messagingProvider;
    private final ExchangeHTTPRequesterI exchangeHTTPRequester;

    public ExchangeService(MessagingProviderI messagingProvider, ExchangeHTTPRequesterI exchangeHTTPRequester) {
        this.messagingProvider = messagingProvider;
        this.exchangeHTTPRequester = exchangeHTTPRequester;
    }

    @Override
    public void cryptoHistoricalPrice(Message message, String crypto) throws IOException, InterruptedException {
        QueryHistoricalPrice queryHistoricalPrice = new QueryHistoricalPrice(crypto, 7);

        HistoricalPriceResponse historicalPrice = this.exchangeHTTPRequester.queryHistoricalPrice(queryHistoricalPrice);

        String txt = "CRYPTO: " + historicalPrice.getName() + "\n" +
                "CURRENCY PAIR: " + historicalPrice.getCurrencyPair() + "\n";
        for (int i = 0; i < historicalPrice.getHistoricPrice().length; i++) {
            CryptoPrice cryptoPrice = historicalPrice.getHistoricPrice()[i];
            txt = txt + cryptoPrice.getDate() + " -> " + cryptoPrice.getPrice() + "\n";
        }

        message.setText(txt);
        this.messagingProvider.sendMessage(message, null);
    }
}

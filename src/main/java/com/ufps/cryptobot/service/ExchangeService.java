package com.ufps.cryptobot.service;

import com.ufps.cryptobot.contract.Message;
import com.ufps.cryptobot.contract.Update;
import com.ufps.cryptobot.controller.ExchangeServiceI;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService implements ExchangeServiceI {

    private MessagingProviderI provider;

    public ExchangeService(MessagingProviderI provider) {
        this.provider = provider;
    }

    @Override
    public void getBitcoin(Update update, String currency) {
        String message = "Bitcoin at: " + "40000 " + currency;
        update.getMessage().setText(message);
        this.provider.sendMessage(update.getMessage());
    }

    @Override
    public void top10Crypto(Update update, String currency) {
        String message = "Currency: " + currency + "\n" +
                "Bitcoin: 40000\n" +
                "Ethereum: 3000\n" +
                "DogeCoin: 0.1\n" +
                "Cardano: 1.2\n" +
                "LiteCoin: 10\n" +
                "BinanceCoin: 500\n" +
                "Tether: 1\n" +
                "Solana:100\n" +
                "BitcoinCash: 5325\n" +
                "Terra: 303";

        update.getMessage().setText(message);
        this.provider.sendMessage(update.getMessage());
    }
}

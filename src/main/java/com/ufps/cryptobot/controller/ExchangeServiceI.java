package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.contract.Update;
import com.ufps.cryptobot.persistence.entity.User;

import java.io.IOException;

public interface ExchangeServiceI {
    void getCryptoValue(Update update, String crypto, String currency, int nDaysAgo) throws IOException, InterruptedException;

    void getMyCrypto(User user, String toCurrency) throws IOException, InterruptedException;

    void registerCrypto(User user, String crypto);
}

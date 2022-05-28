package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.contract.Update;
import com.ufps.cryptobot.persistence.entity.User;

public interface ExchangeServiceI {
    void getCryptoValue(Update update, String crypto, String currency, int nDaysAgo);

    void getMyCrypto(Update update, String toCurrency);

    void registerCrypto(User user, String crypto);
}

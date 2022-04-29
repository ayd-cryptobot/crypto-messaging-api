package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.contract.Update;

public interface ExchangeServiceI {
    void getBitcoin(Update update, String currency);
    void top10Crypto(Update update, String currency);
}

package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.provider.telegram.contract.Message;

public interface ExchangeServiceI {

    void cryptoHistoricalPrice(Message message, String crypto);
}

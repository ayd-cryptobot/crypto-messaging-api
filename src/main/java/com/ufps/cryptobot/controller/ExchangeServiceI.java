package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.provider.telegram.contract.Message;

import java.io.IOException;

public interface ExchangeServiceI {

    void cryptoHistoricalPrice(Message message, String crypto) throws IOException, InterruptedException;
}

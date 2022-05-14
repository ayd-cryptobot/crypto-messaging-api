package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.contract.Update;

import java.io.IOException;

public interface NewsServiceI {
    void getNews(Update update) throws InterruptedException, IOException;
}

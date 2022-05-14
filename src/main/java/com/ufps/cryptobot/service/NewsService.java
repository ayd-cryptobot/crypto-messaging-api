package com.ufps.cryptobot.service;

import com.ufps.cryptobot.contract.Update;
import com.ufps.cryptobot.controller.NewsServiceI;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NewsService implements NewsServiceI {

    private PubSubClientI pubSubClient;

    public NewsService(PubSubClientI pubSubClientI) {
        this.pubSubClient = pubSubClientI;
    }

    @Override
    public void getNews(Update update) throws InterruptedException, IOException {
        this.pubSubClient.publishMessage(update.getMessage().getText());
    }
}

package com.ufps.cryptobot.service;

import com.ufps.cryptobot.contract.Update;
import com.ufps.cryptobot.controller.NewsServiceI;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class NewsService implements NewsServiceI {

    private PubSubClientI pubSubClient;

    public NewsService(PubSubClientI pubSubClientI) {
        this.pubSubClient = pubSubClientI;
    }

    @Override
    public void getNews(Update update) throws InterruptedException, IOException {
        Map<String, String> tags = Map.of("module", "news");
        this.pubSubClient.publishMessage(update.getMessage().getText(), tags);
    }
}

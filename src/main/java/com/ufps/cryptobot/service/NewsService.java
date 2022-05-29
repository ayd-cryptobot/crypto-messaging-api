package com.ufps.cryptobot.service;

import org.json.simple.JSONObject;
import com.ufps.cryptobot.contract.Update;
import com.ufps.cryptobot.controller.NewsServiceI;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class NewsService implements NewsServiceI {

    private PubSubClientI pubSubClient;

    public NewsService(PubSubClientI pubSubClientI) {
        this.pubSubClient = pubSubClientI;
    }

    @Override
    public void getNews(Update update) throws InterruptedException, IOException {
        JSONObject obj = new JSONObject();
        obj.put("chat_id", update.getMessage().getChat().getId());
        String message = obj.toString();

        this.pubSubClient.publishMessage(message, "news");
    }
}

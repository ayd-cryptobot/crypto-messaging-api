package com.ufps.cryptobot.service;

import com.ufps.cryptobot.contract.Update;
import com.ufps.cryptobot.controller.NewsServiceI;
import org.springframework.stereotype.Service;

@Service
public class NewsService implements NewsServiceI {

    private MessagingProviderI provider;

    public NewsService(MessagingProviderI provider) {
        this.provider = provider;
    }

    //TODO solicitar mensaje a noticias
    public void getNews(Update update) {
        String responseText = "did you just say: " + update.getMessage().getText();
        update.getMessage().setText(responseText);
        this.provider.sendMessage(update.getMessage());
    }
}

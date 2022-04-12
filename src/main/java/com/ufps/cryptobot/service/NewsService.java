package com.ufps.cryptobot.service;

import com.ufps.cryptobot.contract.Message;
import com.ufps.cryptobot.contract.NewsMessage;
import com.ufps.cryptobot.contract.Update;
import com.ufps.cryptobot.telegram.Provider;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    private Provider provider;

    public NewsService(Provider provider) {
        this.provider = provider;
    }

    //TODO solicitar mensaje a noticias
    public void getNews(Update update) {
        String responseText = "did you just say: " + update.getMessage().getText();
        update.getMessage().setText(responseText);
        this.provider.sendMessage(update.getMessage());
    }
}

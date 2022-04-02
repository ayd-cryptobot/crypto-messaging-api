package com.ufps.cryptobot.bot;

import com.ufps.cryptobot.contract.Message;
import com.ufps.cryptobot.contract.NewsMessage;
import com.ufps.cryptobot.contract.Update;
import com.ufps.cryptobot.telegram.Provider;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    private Provider provider;
    private final String moneyBagEmoji = "\uD83D\uDD14";
    private final String flagIndicatorEmoji = "\uD83D\uDCCD";

    public NewsService(Provider provider) {
        this.provider = provider;
    }

    public void pushNew(NewsMessage newsMessage) {
        String title = moneyBagEmoji + newsMessage.getTitle();
        String link = flagIndicatorEmoji + newsMessage.getLink();
        String text = title + "\n" + link;
        Message message = new Message(newsMessage.getChatID(), text);
        this.provider.sendMessage(message);
    }

    public void newUpdate(Update update) {
        String responseText = "did you just say: " + update.getMessage().getText();
        update.getMessage().setText(responseText);
        this.provider.sendMessage(update.getMessage());
    }
}

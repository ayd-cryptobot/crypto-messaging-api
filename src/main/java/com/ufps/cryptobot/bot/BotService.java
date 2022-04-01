package com.ufps.cryptobot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.ufps.cryptobot.contract.Update;
import org.springframework.stereotype.Service;

@Service
public class BotService {

    private final TelegramBot bot;

    private int chatId = 1801261524;
    private String token = "5252956900:AAHoHCzSUpRH6ZLJ8M8kK8OvODMcrNZF5-o";

    public BotService() {
        this.bot = new TelegramBot(token);
    }

    public String sendMessage(String message) {
        SendResponse response = bot.execute(new SendMessage(chatId, message));
        return response.toString();
    }

    public void newUpdate(Update update) {
        long chatId = update.getMessage().getChat().getId();
        String messageResponse = "did you just say: " + update.getMessage().getText();
        bot.execute(new SendMessage(chatId, messageResponse));
    }
}

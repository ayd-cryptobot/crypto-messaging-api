package com.ufps.cryptobot.provider.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.ufps.cryptobot.provider.telegram.contract.Message;
import com.ufps.cryptobot.domain.service.MessagingProviderI;
import org.springframework.stereotype.Component;

@Component
public class Provider implements MessagingProviderI {

    private final TelegramBot bot;

    public Provider() {
        this.bot = new TelegramBot(System.getenv("BOT_TOKEN"));
    }

    public SendResponse sendMessage(Message message, String[][] keyboard) {
        SendMessage sendMessage = new SendMessage(message.getChat().getId(), message.getText());

        if(keyboard != null) {
            Keyboard replyKeyBoardMarkup = new ReplyKeyboardMarkup(keyboard);

            sendMessage.replyMarkup(replyKeyBoardMarkup);
        }

        return bot.execute(sendMessage);
    }
}

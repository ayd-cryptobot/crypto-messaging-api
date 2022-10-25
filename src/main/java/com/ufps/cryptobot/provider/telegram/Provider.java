package com.ufps.cryptobot.provider.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import com.ufps.cryptobot.provider.telegram.contract.ImageMessage;
import com.ufps.cryptobot.provider.telegram.contract.Message;
import com.ufps.cryptobot.domain.service.messaging.MessagingProviderI;
import org.springframework.stereotype.Component;

@Component
public class Provider implements MessagingProviderI {

    private final TelegramBot bot;

    public Provider() {
        this.bot = new TelegramBot(System.getenv("BOT_TOKEN"));
    }

    public SendResponse sendMessage(Message message, Keyboard keyboard) {
        SendMessage sendMessage = new SendMessage(message.getChat().getId(), message.getText());

        if (keyboard != null) {
            sendMessage.replyMarkup(keyboard);
        }

        return bot.execute(sendMessage);
    }

    public SendResponse sendImageMessage(ImageMessage imageMessage) {
        SendPhoto sendPhoto = new SendPhoto(imageMessage.getChatID(), imageMessage.getImageLink()).
                caption(imageMessage.getCaption()).parseMode(ParseMode.HTML);

        return bot.execute(sendPhoto);
    }
}

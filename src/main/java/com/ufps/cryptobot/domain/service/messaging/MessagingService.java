package com.ufps.cryptobot.domain.service.messaging;

import com.pengrad.telegrambot.model.request.*;
import com.ufps.cryptobot.controller.rest.contract.DiffuseMessage;
import com.ufps.cryptobot.domain.persistence.entity.User;
import com.ufps.cryptobot.domain.persistence.repository.UserRepository;
import com.ufps.cryptobot.provider.telegram.contract.ImageMessage;
import com.ufps.cryptobot.provider.telegram.contract.Message;
import com.ufps.cryptobot.controller.MessagingServiceI;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessagingService implements MessagingServiceI {

    private MessagingProviderI provider;
    private UserRepository userRepository;

    private final String[][] replyHomeKeyboardTemplate = {
            {"Manage my cryptos", "Manage my account"},
            {"Check historical price of a crypto", "Check news of a crypto"}
    };

    private final String[][] cryptosKeyboardTemplate = {
            {"Bitcoin", "Ethereum", "Dogecoin"},
            {"Cardano", "Litecoin", "Tether"},
            {"Binance coin", "Polkadot", "Ripple"}
    };

    public MessagingService(MessagingProviderI provider, UserRepository userRepository) {
        this.provider = provider;
        this.userRepository = userRepository;
    }

    @Override
    public void sendMessageToUser(Message message) {
        this.provider.sendMessage(message, null);
    }

    @Override
    public void diffuseMessage(DiffuseMessage messageToDiffuse) {
        List<User> users = this.userRepository.findAll();
        for (User user : users) {
            String txt = "<strong>" + messageToDiffuse.getTitle() + "</strong>" +
                    "\n\n" + messageToDiffuse.getBody();

            ImageMessage imageMessage = new ImageMessage(user.getTelegramID(), txt, messageToDiffuse.getImageLink());

            this.provider.sendImageMessage(imageMessage);
        }
    }

    @Override
    public void sendHomeKeyboard(Message message) {
        String invitationMessage = "Hi " + message.getFrom().getFirst_name() + "! Select an option to start";
        message.setText(invitationMessage);

        Keyboard replyKeyBoardMarkup = new ReplyKeyboardMarkup(replyHomeKeyboardTemplate).
                inputFieldPlaceholder("select an option");

        this.provider.sendMessage(message, replyKeyBoardMarkup);
    }

    @Override
    public void sendCryptosKeyboard(Message message) {
        message.setText("this are the available cryptos to check");

        Keyboard replyKeyBoardMarkup = new ReplyKeyboardMarkup(cryptosKeyboardTemplate)
                .inputFieldPlaceholder("select a crypto")
                .oneTimeKeyboard(true);

        this.provider.sendMessage(message, replyKeyBoardMarkup);
    }

    @Override
    public void sendInlineKeyboard(Message message) {
        message.setText("here");

        Keyboard keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("url").url("google.com"),
                        /*new InlineKeyboardButton("List of news").loginUrl(
                                new LoginUrl("google.com")
                        )*/
                });


        this.provider.sendMessage(message, keyboard);
    }
}

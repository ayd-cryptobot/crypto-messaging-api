package com.ufps.cryptobot.domain.service.messaging;

import com.pengrad.telegrambot.model.request.*;
import com.ufps.cryptobot.controller.rest.contract.DiffuseMessage;
import com.ufps.cryptobot.domain.consts.TelegramCommands;
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
    private static final String FRONTEND_HOST = System.getenv("FRONTEND_HOST");
    private static final String frontendManageCryptosEndpoint = "/client/crypto";
    private static final String frontendManageAccountEndpoint = "/client/profile/edit-profile";

    private final String[][] replyHomeKeyboardTemplate = {
            {"Manage my cryptos", "Manage my account"},
            {"Check historical price of a crypto", "Check news"}
    };

    private final String[][] cryptosKeyboardTemplate = {
            {TelegramCommands.bitcoin, TelegramCommands.ethereum, TelegramCommands.dogecoin},
            {TelegramCommands.cardano, TelegramCommands.litecoin, TelegramCommands.tether},
            {TelegramCommands.binanceCoin, TelegramCommands.polkadot, TelegramCommands.ripple}
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
    public void sendLoginInlineKeyboard(Message message, String text, String view) {
        message.setText(text);

        String endpoint = "";
        switch (view){
            case "manage-account":{
                endpoint = frontendManageAccountEndpoint;
                break;
            }
            case "manage-cryptos":{
                endpoint = frontendManageCryptosEndpoint;
                break;
            }
        }

        Keyboard keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton("login").loginUrl(
                        //new LoginUrl("https://da63-181-132-0-23.ngrok.io/messaging/login") usar para probar autenticación de token de telegram
                        new LoginUrl(FRONTEND_HOST + endpoint).botUsername("@UfpsTestBot")
                )
        );

        this.provider.sendMessage(message, keyboard);
    }

    @Override
    public void sendCheckNewsLink(Message message) {
        String url = FRONTEND_HOST + "/client/news";
        message.setText("The best news here");

        Keyboard keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Crypto News").url(url));

        this.provider.sendMessage(message, keyboard);
    }
}

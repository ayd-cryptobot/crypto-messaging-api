package com.ufps.cryptobot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.gax.rpc.ApiException;
import com.ufps.cryptobot.controller.mapper.PubSubEventMapper;
import com.ufps.cryptobot.provider.telegram.contract.Message;
import com.ufps.cryptobot.provider.pubsub.contract.PubSubMessage;
import com.ufps.cryptobot.provider.telegram.contract.Update;
import com.ufps.cryptobot.domain.consts.TelegramCommands;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("messaging")
public class MessagesController {

    private PubSubEventMapper pubSubEventMapper;
    private AccountsServiceI accountsServiceI;
    private MessagingServiceI messagingService;


    public MessagesController(AccountsServiceI usersServiceI, MessagingServiceI messagingService,
                              PubSubEventMapper pubSubEventMapper) {
        this.accountsServiceI = usersServiceI;
        this.messagingService = messagingService;
        this.pubSubEventMapper = pubSubEventMapper;
    }

    @PostMapping("/message/send")
    public ResponseEntity<String> sendMessageFromPubSubEvent(@RequestBody PubSubMessage pubSubMessage) {
        try {
            Message message = this.pubSubEventMapper.pubSubEventToTelegramMessage(pubSubMessage.getEvent());
            this.messagingService.sendMessageToUser(message);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Incorrect JSON message for sending messages", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> getUpdate(@RequestBody Update update) {
        try {
            switch (update.getMessage().getText()) {
                case TelegramCommands.startCommand:
                    this.accountsServiceI.callAccountsToRegisterAccount(update.getMessage().getFrom());
                default:
                    this.messagingService.sendHomeKeyboard(update);
            }
        } catch (ApiException e) {
            return new ResponseEntity<>("Error publishing message", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

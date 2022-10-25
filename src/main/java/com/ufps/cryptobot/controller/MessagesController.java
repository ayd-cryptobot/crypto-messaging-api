package com.ufps.cryptobot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.gax.rpc.ApiException;
import com.ufps.cryptobot.controller.mapper.PubSubEventMapper;
import com.ufps.cryptobot.controller.rest.contract.DiffuseMessage;
import com.ufps.cryptobot.provider.telegram.contract.Message;
import com.ufps.cryptobot.provider.pubsub.contract.PubSubMessage;
import com.ufps.cryptobot.provider.telegram.contract.Update;
import com.ufps.cryptobot.domain.consts.TelegramCommands;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import javax.validation.Valid;

@RestController
@RequestMapping("messaging")
public class MessagesController {

    private PubSubEventMapper pubSubEventMapper;

    private AccountsServiceI accountsServiceI;
    private MessagingServiceI messagingService;
    private ExchangeServiceI exchangeServiceI;


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

    @PostMapping("/message/diffuse")
    public ResponseEntity<String> diffuseMessage(@Valid @RequestBody DiffuseMessage messageToDiffuse) {
        this.messagingService.diffuseMessage(messageToDiffuse);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> getUpdate(@RequestBody Update update) {
        try {
            switch (update.getMessage().getText()) {
                case TelegramCommands.startCommand:
                    this.accountsServiceI.callAccountsToRegisterAccount(update.getMessage().getFrom());
                case TelegramCommands.manageCryptosMessage:
                    //TODO manage login and return redirection
                    break;
                case TelegramCommands.manageAccountMessage:
                    //TODO manage login and return redirection
                    break;
                case TelegramCommands.checkHistoricalPriceOfACryptoMessage:
                    this.messagingService.sendCryptosKeyboard(update.getMessage());
                    break;
                case TelegramCommands.checkNewsOfACrypto:
                    //TODO return redirection to the news
                    //update.getMessage().setText("google.com");
                    //this.messagingService.sendMessageToUser(update.getMessage());
                    this.messagingService.sendInlineKeyboard(update.getMessage());
                    break;
                case TelegramCommands.bitcoin:
                case TelegramCommands.ethereum:
                case TelegramCommands.dogecoin:
                case TelegramCommands.cardano:
                case TelegramCommands.litecoin:
                case TelegramCommands.tether:
                case TelegramCommands.binanceCoin:
                case TelegramCommands.polkadot:
                case TelegramCommands.ripple:
                    this.exchangeServiceI.cryptoHistoricalPrice(update.getMessage(), update.getMessage().getText());
                    break;
                default:
                    this.messagingService.sendHomeKeyboard(update.getMessage());
            }
        } catch (ApiException e) {
            return new ResponseEntity<>("Error publishing message", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

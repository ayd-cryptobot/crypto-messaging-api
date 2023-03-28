package com.ufps.cryptobot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.gax.rpc.ApiException;
import com.ufps.cryptobot.controller.mapper.PubSubEventMapper;
import com.ufps.cryptobot.controller.rest.contract.Auth;
import com.ufps.cryptobot.controller.rest.contract.DiffuseMessage;
import com.ufps.cryptobot.provider.telegram.contract.Message;
import com.ufps.cryptobot.provider.pubsub.contract.PubSubMessage;
import com.ufps.cryptobot.provider.telegram.contract.Update;
import com.ufps.cryptobot.domain.consts.TelegramCommands;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.view.RedirectView;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("messaging")
public class MessagesController {

    private final PubSubEventMapper pubSubEventMapper;

    private final AccountsServiceI accountsService;
    private final MessagingServiceI messagingService;
    private final ExchangeServiceI exchangeService;
    private final QAServiceI qaService;


    public MessagesController(PubSubEventMapper pubSubEventMapper, AccountsServiceI accountsServiceI,
                              MessagingServiceI messagingService, ExchangeServiceI exchangeServiceI,
                              QAServiceI qaServiceI) {
        this.pubSubEventMapper = pubSubEventMapper;
        this.accountsService = accountsServiceI;
        this.messagingService = messagingService;
        this.exchangeService = exchangeServiceI;
        this.qaService = qaServiceI;
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

    @CrossOrigin
    @PostMapping("/message/diffuse")
    public ResponseEntity<String> diffuseMessage(@Valid @RequestBody DiffuseMessage messageToDiffuse) {
        this.messagingService.diffuseMessage(messageToDiffuse);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> getUpdate(@RequestBody Update update) {
        if (update.getMessage() != null) {
            try {
                switch (update.getMessage().getText()) {
                    case TelegramCommands.manageCryptosMessage:
                        this.messagingService.sendLoginInlineKeyboard(update.getMessage(), "Login to manage your cryptos here", "manage-cryptos");
                        break;
                    case TelegramCommands.manageAccountMessage:
                        this.messagingService.sendLoginInlineKeyboard(update.getMessage(), "Login to manage your account", "manage-account");
                        break;
                    case TelegramCommands.checkHistoricalPriceOfACryptoMessage:
                        this.messagingService.sendCryptosKeyboard(update.getMessage());
                        break;
                    case TelegramCommands.checkNewsMessage:
                        this.messagingService.sendCheckNewsLink(update.getMessage());
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
                        this.exchangeService.cryptoHistoricalPrice(update.getMessage(), update.getMessage().getText());
                        this.messagingService.sendHomeKeyboard(update.getMessage());
                        break;
                    case TelegramCommands.startCommand:
                        this.accountsService.callAccountsToRegisterAccount(update.getMessage().getFrom());
                    default:
                        this.qaService.sendQuestionToAI(update.getMessage());
                        update.getMessage().setText("¿Qué más necesitas?");
                        this.messagingService.sendHomeKeyboard(update.getMessage());
                }
            } catch (ApiException e) {
                return new ResponseEntity<>("Error publishing message", HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (IOException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

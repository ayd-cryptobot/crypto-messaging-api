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
import org.springframework.web.servlet.view.RedirectView;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("messaging")
public class MessagesController {

    private PubSubEventMapper pubSubEventMapper;

    private AccountsServiceI accountsServiceI;
    private MessagingServiceI messagingService;
    private ExchangeServiceI exchangeServiceI;


    public MessagesController(PubSubEventMapper pubSubEventMapper, AccountsServiceI accountsServiceI, MessagingServiceI messagingService, ExchangeServiceI exchangeServiceI) {
        this.pubSubEventMapper = pubSubEventMapper;
        this.accountsServiceI = accountsServiceI;
        this.messagingService = messagingService;
        this.exchangeServiceI = exchangeServiceI;
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
        try {
            switch (update.getMessage().getText()) {
                case TelegramCommands.manageCryptosMessage:
                    //TODO manage login and return redirection
                    this.messagingService.sendLoginInlineKeyboard(update.getMessage(), "Login to manage your cryptos here");
                    break;
                case TelegramCommands.manageAccountMessage:
                    //TODO manage login and return redirection
                    this.messagingService.sendLoginInlineKeyboard(update.getMessage(), "Login to manage your account");
                    break;
                case TelegramCommands.checkHistoricalPriceOfACryptoMessage:
                    this.messagingService.sendCryptosKeyboard(update.getMessage());
                    break;
                case TelegramCommands.checkNewsOfACrypto:
                    //TODO return redirection to the news
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
                    this.messagingService.sendHomeKeyboard(update.getMessage());
                    break;
                case TelegramCommands.startCommand:
                    this.accountsServiceI.callAccountsToRegisterAccount(update.getMessage().getFrom());
                default:
                    this.messagingService.sendHomeKeyboard(update.getMessage());
            }
        } catch (ApiException e) {
            return new ResponseEntity<>("Error publishing message", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/login")
    public RedirectView telegramAuth(@RequestParam String id, @RequestParam(name = "first_name") String firstName,
                                     @RequestParam String username, @RequestParam(name = "auth_date") String authDate,
                                     @RequestParam String hash) {
        String dataCheckString = "auth_date=" + authDate + "\n" +
                "first_name=" + firstName + "\n" +
                "id=" + id + "\n" +
                "username=" + username;

        try {
            SecretKeySpec secretKey = new SecretKeySpec(
                    MessageDigest.getInstance("SHA-256").digest("5252956900:AAHoHCzSUpRH6ZLJ8M8kK8OvODMcrNZF5-o".getBytes(StandardCharsets.UTF_8)
                    ), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);

            byte[] result = mac.doFinal(dataCheckString.getBytes(StandardCharsets.UTF_8));

            String resultStr = this.bytesToHex(result);

            if (hash.equals(resultStr)) {
                System.out.println("auth successfully done");
                //TODO validar que exista el usuario


            } else {
                return new RedirectView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String JWTToken = "02i3ygvf0uv345g0y834v5g790";
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("token", JWTToken);

        RedirectView response = new RedirectView("https://89cb-179-32-178-138.ngrok.io");
        response.setAttributesMap(attributes);
        return response;
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

package com.ufps.cryptobot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.gax.rpc.ApiException;
import com.ufps.cryptobot.contract.Message;
import com.ufps.cryptobot.contract.PubSubMessage;
import com.ufps.cryptobot.mapper.ExchangeMapper;
import com.ufps.cryptobot.mapper.NewsMapper;
import com.ufps.cryptobot.contract.Update;
import com.ufps.cryptobot.persistence.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@RestController
@RequestMapping("messages")
public class MessageEventsController {

    private NewsMapper newsMapper;
    private ExchangeMapper exchangeMapper;

    private UsersServiceI usersServiceI;

    private NewsServiceI newsService;
    private MessagingServiceI messagingService;
    private ExchangeServiceI exchangeService;

    private final String getNewsCommand = "/getnews";
    private final String getCrypto = "/getcrypto";
    private final String getBitcoin = "/getbitcoin";
    private final String getEthereum = "/getethereum";
    private final String registerBTC = "/registerbtc";
    private final String registerETH = "/registereth";
    private final String registerUSDT = "/registerusdt";
    private final String registerADA = "/registerada";
    private final String registerSOL = "/registersol";

    private final String BTC = "BTC";
    private final String ETH = "ETH";
    private final String USDT = "USDT";
    private final String ADA = "ADA";
    private final String SOL = "SOL";


    public MessageEventsController(NewsMapper newsMapper, ExchangeMapper exchangeMapper, UsersServiceI usersServiceI,
                                   NewsServiceI newsService, MessagingServiceI messagingService, ExchangeServiceI exchangeService) {
        this.newsMapper = newsMapper;
        this.exchangeMapper = exchangeMapper;
        this.usersServiceI = usersServiceI;
        this.newsService = newsService;
        this.messagingService = messagingService;
        this.exchangeService = exchangeService;
    }

    @PostMapping("/news/send")
    public ResponseEntity<String> sendNewsMessage(@RequestBody PubSubMessage pubSubMessage) {
        try {
            Message message = this.newsMapper.NewsMessageEventToMessage(pubSubMessage.getEvent());
            this.messagingService.pushMessageToUser(message);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("fail during JSON parsing", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("exchange/send")
    public ResponseEntity<String> sendExchangeMessage(@RequestBody PubSubMessage pubSubMessage) {
        try {
            Message message = this.exchangeMapper.ExchangeMessageEventToMessage(pubSubMessage.getEvent());
            this.messagingService.pushMessageToUser(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException i){
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> getUpdate(@RequestBody Update update) {
        User user = this.usersServiceI.findUser(update.getMessage().getFrom());

        try {
            switch (update.getMessage().getText()) {
                case getNewsCommand:
                    this.newsService.getNews(update);
                    break;
                case getCrypto:
                    this.exchangeService.getMyCrypto(user, "USD");
                    break;
                case getBitcoin:
                    this.exchangeService.getCryptoValue(update, "bitcoin", "USD", 7);
                    break;
                case getEthereum:
                    this.exchangeService.getCryptoValue(update, "ethereum", "USD", 7);
                    break;
                case registerBTC:
                    this.exchangeService.registerCrypto(user, BTC);
                    break;
                case registerETH:
                    this.exchangeService.registerCrypto(user, ETH);
                    break;
                case registerUSDT:
                    this.exchangeService.registerCrypto(user, USDT);
                    break;
                case registerADA:
                    this.exchangeService.registerCrypto(user, ADA);
                    break;
                case registerSOL:
                    this.exchangeService.registerCrypto(user, SOL);
                    break;
                default:
                    this.messagingService.pushUnrecognizedCommand(update);
            }
        } catch (InterruptedException e) {
            return new ResponseEntity<>("Error during shutdown of publisher", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            return new ResponseEntity<>("Error creating publisher", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ApiException e) {
            return new ResponseEntity<>("Error publishing message", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

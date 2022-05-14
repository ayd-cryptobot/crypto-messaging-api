package com.ufps.cryptobot.controller;

import com.google.api.gax.rpc.ApiException;
import com.ufps.cryptobot.contract.Message;
import com.ufps.cryptobot.mapper.NewsMapper;
import com.ufps.cryptobot.contract.NewsMessage;
import com.ufps.cryptobot.contract.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@RestController
@RequestMapping("messages")
public class MessageEventsController {

    private NewsMapper newsMapper;
    private NewsServiceI newsService;
    private MessagingServiceI messagingService;
    private ExchangeServiceI exchangeService;

    private final String getNewsCommand = "/getnews";
    private final String top10CryptoUSD = "/top10cryptousd";
    private final String top10CryptoCOP = "/top10cryptocop";
    private final String getBitcoin = "/getbitcoin";

    public MessageEventsController(NewsMapper newsMapper, NewsServiceI newsService, MessagingServiceI messagingService,
                                   ExchangeServiceI exchangeService) {
        this.newsMapper = newsMapper;
        this.newsService = newsService;
        this.messagingService = messagingService;
        this.exchangeService = exchangeService;
    }

    @PostMapping("/news/send")
    public ResponseEntity<String> sendNewsMessage(@RequestBody NewsMessage newsMessage) {
        Message message = this.newsMapper.NewsMessageToMessage(newsMessage);

        this.messagingService.pushMessageToUser(message);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> getUpdate(@RequestBody Update update) {
        try {
            switch (update.getMessage().getText()) {
                case getNewsCommand:
                    this.newsService.getNews(update);
                    break;
                case top10CryptoUSD:
                    this.exchangeService.top10Crypto(update, "USD");
                    break;
                case top10CryptoCOP:
                    this.exchangeService.top10Crypto(update, "COP");
                    break;
                case getBitcoin:
                    this.exchangeService.getBitcoin(update, "USD");
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

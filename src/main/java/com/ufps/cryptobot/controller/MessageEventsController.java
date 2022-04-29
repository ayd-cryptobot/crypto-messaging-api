package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.contract.Message;
import com.ufps.cryptobot.mapper.NewsMapper;
import com.ufps.cryptobot.contract.NewsMessage;
import com.ufps.cryptobot.contract.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

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
        switch (update.getMessage().getText()) {
            case getNewsCommand:
                this.newsService.getNews(update);
                break;
            case top10CryptoUSD:
                //TODO
                break;
            case top10CryptoCOP:
                //TODO
                break;
            case getBitcoin:
                //TODO
                break;
            default:
                this.messagingService.pushUnrecognizedCommand(update);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

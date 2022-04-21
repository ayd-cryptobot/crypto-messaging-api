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

    private NewsServiceI newsService;
    private NewsMapper newsMapper;
    private MessagingServiceI messagingService;

    private final String getNewsCommand = "/getnews";

    public MessageEventsController(NewsServiceI newsService, NewsMapper newsMapper, MessagingServiceI badRequestService) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
        this.messagingService = badRequestService;
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
            default:
                this.messagingService.pushUnrecognizedCommand(update);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

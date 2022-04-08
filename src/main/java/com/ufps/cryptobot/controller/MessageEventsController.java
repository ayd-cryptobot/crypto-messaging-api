package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.service.BadRequestService;
import com.ufps.cryptobot.service.NewsService;
import com.ufps.cryptobot.contract.NewsMessage;
import com.ufps.cryptobot.contract.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("messages")
public class MessageEventsController {

    private NewsService newsService;
    private BadRequestService badRequestService;

    private final String getNewsCommand = "/getNews";

    public MessageEventsController(NewsService newsService, BadRequestService badRequestService) {
        this.newsService = newsService;
        this.badRequestService = badRequestService;
    }

    @PostMapping("/news/send")
    public ResponseEntity<String> sendNewsMessage(@RequestBody NewsMessage message) {
        this.newsService.pushNew(message);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> getUpdate(@RequestBody Update update) {
        switch (update.getMessage().getText()){
            case getNewsCommand:
                this.newsService.newUpdate(update);
                break;
            default:
                this.badRequestService.pushUnrecognizedCommand(update);
        }

        return new ResponseEntity<>("OK",HttpStatus.OK);
    }
}

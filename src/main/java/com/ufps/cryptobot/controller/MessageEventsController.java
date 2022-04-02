package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.bot.NewsService;
import com.ufps.cryptobot.contract.NewsMessage;
import com.ufps.cryptobot.contract.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("messages")
public class MessageEventsController {

    private NewsService newsService;

    public MessageEventsController(NewsService botService) {
        this.newsService = botService;
    }

    @PostMapping("/news/send")
    public ResponseEntity<String> sendNewsMessage(@RequestBody NewsMessage message) {
        this.newsService.pushNew(message);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> getUpdate(@RequestBody Update update) {
        this.newsService.newUpdate(update);
        return new ResponseEntity<>("OK",HttpStatus.OK);
    }
}

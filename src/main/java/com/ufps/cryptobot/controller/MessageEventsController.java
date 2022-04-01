package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.bot.BotService;
import com.ufps.cryptobot.contract.Message;
import com.ufps.cryptobot.contract.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("messages")
public class MessageEventsController {

    private BotService botService;

    public MessageEventsController(BotService botService) {
        this.botService = botService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody Message message) {
        String result = this.botService.sendMessage(message);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> getUpdate(@RequestBody Update update) {
        this.botService.newUpdate(update);
        return new ResponseEntity<>("ok",HttpStatus.OK);
    }
}

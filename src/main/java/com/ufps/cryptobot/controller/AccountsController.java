package com.ufps.cryptobot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ufps.cryptobot.controller.mapper.PubSubEventMapper;
import com.ufps.cryptobot.controller.rest.contract.AccountEvent;
import com.ufps.cryptobot.controller.rest.contract.Auth;
import com.ufps.cryptobot.provider.pubsub.contract.PubSubMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("messaging")
public class AccountsController {

    private PubSubEventMapper pubSubEventMapper;
    private AccountsServiceI accountsService;

    public AccountsController(PubSubEventMapper pubSubEventMapper, AccountsServiceI accountsServiceI) {
        this.pubSubEventMapper = pubSubEventMapper;
        this.accountsService = accountsServiceI;
    }

    @PostMapping("/account/event")
    public ResponseEntity<String> processAccountEvent(@RequestBody PubSubMessage pubSubMessage) {
        try {
            AccountEvent accountEvent = this.pubSubEventMapper.pubSubEventToAccountEvent(pubSubMessage.getEvent());
            switch (accountEvent.getOperationType()){
                case "create":
                    this.accountsService.saveAccount(accountEvent);
                    break;
                case "update":
                    this.accountsService.updateAccount(accountEvent);
                    break;
                case "delete":
                    this.accountsService.deleteAccount(accountEvent.getTelegramID());
                    break;
            }
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Incorrect account JSON event", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("event processed", HttpStatus.OK);
    }

    @GetMapping("/login")
    public RedirectView telegramAuth(@RequestParam String id, @RequestParam(name = "first_name") String firstName,
                                     @RequestParam String username, @RequestParam(name = "auth_date") String authDate,
                                     @RequestParam String hash) {
        Auth auth = new Auth(id, firstName, username, authDate, hash);

        boolean isAuthorized = false;
        try {
            isAuthorized = this.accountsService.authAccount(auth);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        if (!isAuthorized) {
            //TODO bad redirect
        }

        //TODO good redirect
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("t-token", hash);

        RedirectView response = new RedirectView("https://www.google.com.co/");
        response.setAttributesMap(attributes);
        return response;
    }
}

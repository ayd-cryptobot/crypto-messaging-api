package com.ufps.cryptobot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ufps.cryptobot.controller.mapper.PubSubEventMapper;
import com.ufps.cryptobot.controller.rest.contract.AccountEvent;
import com.ufps.cryptobot.provider.pubsub.contract.PubSubMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("messaging")
public class AccountsController {

    private PubSubEventMapper pubSubEventMapper;
    private AccountsServiceI accountsServiceI;

    public AccountsController(PubSubEventMapper pubSubEventMapper, AccountsServiceI accountsServiceI) {
        this.pubSubEventMapper = pubSubEventMapper;
        this.accountsServiceI = accountsServiceI;
    }

    @PostMapping("/account/event")
    public ResponseEntity<String> processAccountEvent(@RequestBody PubSubMessage pubSubMessage) {
        try {
            AccountEvent accountEvent = this.pubSubEventMapper.pubSubEventToAccountEvent(pubSubMessage.getEvent());
            switch (accountEvent.getOperationType()){
                case "create":
                    this.accountsServiceI.saveAccount(accountEvent);
                    break;
                case "update":
                    this.accountsServiceI.updateAccount(accountEvent);
                    break;
                case "delete":
                    this.accountsServiceI.deleteAccount(accountEvent.getTelegramID());
                    break;
            }
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Incorrect account JSON event", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("event processed", HttpStatus.OK);
    }
}

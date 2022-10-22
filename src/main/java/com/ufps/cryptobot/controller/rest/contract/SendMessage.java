package com.ufps.cryptobot.controller.rest.contract;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendMessage {
    @JsonAlias("chat_id")
    private Long chatID;

    @JsonAlias("message")
    private String message;
}

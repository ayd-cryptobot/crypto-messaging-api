package com.ufps.cryptobot.contract;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @JsonAlias("data")
    String data;

    @JsonAlias("message_id")
    String messageId;
}

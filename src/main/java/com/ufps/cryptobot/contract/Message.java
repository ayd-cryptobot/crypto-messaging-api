package com.ufps.cryptobot.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Message {
    private Integer message_id;
    private Chat chat;
    private String text;
}

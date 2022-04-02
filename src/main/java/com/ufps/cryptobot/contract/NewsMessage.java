package com.ufps.cryptobot.contract;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class NewsMessage {
    @JsonAlias("chat_id")
    private Long chatID;
    private String title;
    private String link;
}

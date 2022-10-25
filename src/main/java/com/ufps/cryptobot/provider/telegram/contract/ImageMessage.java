package com.ufps.cryptobot.provider.telegram.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageMessage {
    private Long chatID;
    private String caption;
    private String imageLink;
}

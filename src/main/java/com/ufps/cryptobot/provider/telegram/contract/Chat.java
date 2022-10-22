package com.ufps.cryptobot.provider.telegram.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Chat {
    private Long id;
    private String type;

    public Chat(Long id) {
        this.id = id;
    }
}

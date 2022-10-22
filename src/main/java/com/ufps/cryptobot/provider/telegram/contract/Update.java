package com.ufps.cryptobot.provider.telegram.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Update {
    private Integer update_id;
    private Message message;
}

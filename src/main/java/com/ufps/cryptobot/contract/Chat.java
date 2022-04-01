package com.ufps.cryptobot.contract;

import com.ufps.cryptobot.Type.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Chat {
    private Long id;
    private Type type;
}

package com.ufps.cryptobot.contract.exchange;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ExchangeMessage {
    @JsonAlias("chat_id")
    private Long chatID;

    @JsonAlias("cryptos")
    private List<Crypto> cryptos;
}

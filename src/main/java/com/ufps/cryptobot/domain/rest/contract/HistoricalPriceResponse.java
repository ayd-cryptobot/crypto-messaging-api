package com.ufps.cryptobot.domain.rest.contract;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoricalPriceResponse {
    @JsonAlias("name")
    private String name;

    @JsonAlias("currency_pair")
    private String currencyPair;

    @JsonAlias("historic_price")
    private CryptoPrice[] historicPrice;
}

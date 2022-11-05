package com.ufps.cryptobot.domain.rest.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryHistoricalPrice {
    private String crypto;
    //TODO add initial and final date
}

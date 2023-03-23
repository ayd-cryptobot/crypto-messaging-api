package com.ufps.cryptobot.domain.rest.contract.query;

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
    private int dateRange;
}

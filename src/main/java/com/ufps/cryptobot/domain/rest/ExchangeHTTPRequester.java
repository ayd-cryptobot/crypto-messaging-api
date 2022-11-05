package com.ufps.cryptobot.domain.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufps.cryptobot.domain.rest.contract.QueryHistoricalPrice;
import com.ufps.cryptobot.domain.service.exchange.ExchangeHTTPRequesterI;
import org.springframework.stereotype.Component;

@Component
public class ExchangeHTTPRequester implements ExchangeHTTPRequesterI {

    private ObjectMapper objectMapper;

    public ExchangeHTTPRequester() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void queryHistoricalPrice(QueryHistoricalPrice queryHistoricalPrice) {
        //TODO
    }
}

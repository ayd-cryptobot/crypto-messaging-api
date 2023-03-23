package com.ufps.cryptobot.domain.service.exchange;

import com.ufps.cryptobot.domain.rest.contract.response.HistoricalPriceResponse;
import com.ufps.cryptobot.domain.rest.contract.query.QueryHistoricalPrice;

import java.io.IOException;

public interface ExchangeHTTPRequesterI {
    public HistoricalPriceResponse queryHistoricalPrice(QueryHistoricalPrice queryHistoricalPrice) throws IOException, InterruptedException;
}

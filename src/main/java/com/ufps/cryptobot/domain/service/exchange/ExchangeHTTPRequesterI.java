package com.ufps.cryptobot.domain.service.exchange;

import com.ufps.cryptobot.domain.rest.contract.HistoricalPriceResponse;
import com.ufps.cryptobot.domain.rest.contract.QueryHistoricalPrice;

import java.io.IOException;
import java.net.MalformedURLException;

public interface ExchangeHTTPRequesterI {
    public HistoricalPriceResponse queryHistoricalPrice(QueryHistoricalPrice queryHistoricalPrice) throws IOException, InterruptedException;
}

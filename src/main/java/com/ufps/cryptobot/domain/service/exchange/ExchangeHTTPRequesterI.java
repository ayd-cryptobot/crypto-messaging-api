package com.ufps.cryptobot.domain.service.exchange;

import com.ufps.cryptobot.domain.rest.contract.QueryHistoricalPrice;

public interface ExchangeHTTPRequesterI {
    public void queryHistoricalPrice(QueryHistoricalPrice queryHistoricalPrice);
}

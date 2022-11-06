package com.ufps.cryptobot.domain.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufps.cryptobot.domain.rest.contract.HistoricalPriceResponse;
import com.ufps.cryptobot.domain.rest.contract.QueryHistoricalPrice;
import com.ufps.cryptobot.domain.service.exchange.ExchangeHTTPRequesterI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class ExchangeHTTPRequester implements ExchangeHTTPRequesterI {

    private static final String queryHistoricPriceEndpoint = "/exchange/crypto/price";
    private static final String exchangeHost = System.getenv("EXCHANGE_HOST");

    private ObjectMapper objectMapper;

    public ExchangeHTTPRequester() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public HistoricalPriceResponse queryHistoricalPrice(QueryHistoricalPrice queryHistoricalPrice) throws IOException {
        String url = exchangeHost + queryHistoricPriceEndpoint;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

        String jsonString = this.objectMapper.writeValueAsString(queryHistoricalPrice);

        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(jsonString.getBytes());
        os.flush();
        os.close();

        int responseCode = con.getResponseCode();
        System.out.println("query historical price POST Response Code :: " + responseCode);

        this.validateResponse(responseCode);

        return this.objectMapper.readValue(con.getInputStream(), HistoricalPriceResponse.class);
    }

    private void validateResponse(int responseCode) throws RuntimeException {
        if (responseCode != HttpStatus.OK.value()) {
            System.out.println("get historical price POST request got a bad response");

            throw new RuntimeException("Bad status code response: " + responseCode);
        }

        System.out.println("prices found");
    }
}

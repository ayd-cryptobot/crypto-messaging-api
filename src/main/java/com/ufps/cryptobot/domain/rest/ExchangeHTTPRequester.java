package com.ufps.cryptobot.domain.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufps.cryptobot.domain.rest.contract.response.CryptoPrice;
import com.ufps.cryptobot.domain.rest.contract.response.HistoricalPriceResponse;
import com.ufps.cryptobot.domain.rest.contract.query.QueryHistoricalPrice;
import com.ufps.cryptobot.domain.service.exchange.ExchangeHTTPRequesterI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ExchangeHTTPRequester implements ExchangeHTTPRequesterI {

    private static final String queryHistoricPriceEndpoint = "/exchange/crypto/price";
    private static final String exchangeHost = "https://cryptobot-345516.ue.r.appspot.com";

    private final ObjectMapper objectMapper;

    public ExchangeHTTPRequester() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public HistoricalPriceResponse queryHistoricalPrice(QueryHistoricalPrice queryHistoricalPrice) throws IOException, InterruptedException {
        String url = exchangeHost + queryHistoricPriceEndpoint;
        String jsonString = this.objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(queryHistoricalPrice);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(body)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("query historical price POST Response Code :: " + response.statusCode());

        try {
            this.validateResponse(response.statusCode());
        }catch(RuntimeException e){
            return new HistoricalPriceResponse("Error getting the historical price", "", new CryptoPrice[]{});
        }


        return this.objectMapper.readValue(response.body(), HistoricalPriceResponse.class);
    }

    private void validateResponse(int responseCode) throws RuntimeException {
        if (responseCode != HttpStatus.OK.value()) {
            System.out.println("get historical price POST request got a bad response");

            throw new RuntimeException("Bad status code response: " + responseCode);
        }

        System.out.println("prices found");
    }
}

package com.ufps.cryptobot.domain.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufps.cryptobot.domain.rest.contract.query.QueryGenerateText;
import com.ufps.cryptobot.domain.rest.contract.response.TextGeneratedResponse;
import com.ufps.cryptobot.domain.service.qa.QAHTTPRequesterI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class QAHTTPRequester implements QAHTTPRequesterI{

    private static final String iaGenerateTextEndPoint = "/qa/ia/text-generator";
    private static final String qaHost = System.getenv("QA_HOST");

    private final ObjectMapper objectMapper;

    public QAHTTPRequester() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public TextGeneratedResponse requestGenerateText(QueryGenerateText queryGenerateText) throws IOException, InterruptedException {
        String url = qaHost + iaGenerateTextEndPoint;
        String jsonString = this.objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(queryGenerateText);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(body)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("request generate text POST Response Code :: " + response.statusCode());

        this.validateResponse(response.statusCode());

        return this.objectMapper.readValue(response.body(), TextGeneratedResponse.class);
    }

    private void validateResponse(int responseCode) throws RuntimeException {
        if (responseCode != HttpStatus.OK.value()) {
            System.out.println("generate text POST request got a bad response");

            throw new RuntimeException("Bad status code response: " + responseCode);
        }

        System.out.println("text generated successfully");
    }
}

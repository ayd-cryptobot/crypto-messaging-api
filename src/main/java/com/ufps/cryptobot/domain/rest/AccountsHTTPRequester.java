package com.ufps.cryptobot.domain.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufps.cryptobot.domain.rest.contract.query.CreateAccount;
import com.ufps.cryptobot.domain.service.accounts.AccountsHTTPRequesterI;
import com.ufps.cryptobot.provider.telegram.contract.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class AccountsHTTPRequester implements AccountsHTTPRequesterI {

    private static final String createAccountEndpoint = "/accounts/create";
    private static final String accountsHost = "https://accounts-dot-cryptobot-345516.ue.r.appspot.com";

    private final ObjectMapper objectMapper;

    public AccountsHTTPRequester() {
        this.objectMapper = new ObjectMapper();
    }

    public void createAccount(User user) throws IOException, InterruptedException {
        String url = accountsHost + createAccountEndpoint;
        CreateAccount createAccount = new CreateAccount(user.getId(), user.getFirst_name(), user.getLast_name(),
                user.getUsername(), "cliente");
        String jsonString = this.objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(createAccount);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(body)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Create account POST Response Code :: " + response.statusCode());

        this.validateResponse(response.statusCode());
    }

    public void validateResponse(int responseCode) throws RuntimeException {
        if (responseCode == HttpStatus.ALREADY_REPORTED.value()) {
            System.out.println("Account already created");
            return;
        }
        if (responseCode != HttpStatus.OK.value()) {
            System.out.println("create account POST request got a bad response");

            throw new RuntimeException("Bad status code response: " + responseCode);
        }

        System.out.println("Account created");
    }
}

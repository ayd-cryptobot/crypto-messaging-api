package com.ufps.cryptobot.domain.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufps.cryptobot.domain.rest.contract.CreateAccount;
import com.ufps.cryptobot.domain.service.accounts.AccountsHTTPRequesterI;
import com.ufps.cryptobot.provider.telegram.contract.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class AccountsHTTPRequester implements AccountsHTTPRequesterI {

    private static final String createAccountEndpoint = "/accounts/create";
    private static final String accountsHost = System.getenv("ACCOUNTS_HOST");

    private ObjectMapper objectMapper;

    public AccountsHTTPRequester() {
        this.objectMapper = new ObjectMapper();
    }

    public void createAccount(User user) throws IOException {
        String url = accountsHost + createAccountEndpoint;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

        CreateAccount createAccount = new CreateAccount(user.getId(), user.getFirst_name(), user.getLast_name(),
                user.getUsername(), "cliente");
        String jsonString = this.objectMapper.writeValueAsString(createAccount);

        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(jsonString.getBytes());
        os.flush();
        os.close();

        int responseCode = con.getResponseCode();
        System.out.println("Create account POST Response Code :: " + responseCode);

        this.validateResponse(responseCode);
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

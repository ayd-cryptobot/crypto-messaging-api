package com.ufps.cryptobot.domain.service.accounts;

import com.ufps.cryptobot.provider.telegram.contract.User;

import java.io.IOException;
import java.net.MalformedURLException;

public interface AccountsHTTPRequesterI {
    public void createAccount(User user) throws IOException, InterruptedException;
}

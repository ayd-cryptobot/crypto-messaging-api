package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.controller.rest.contract.AccountEvent;
import com.ufps.cryptobot.provider.telegram.contract.User;

import java.io.IOException;

public interface AccountsServiceI {
    void callAccountsToRegisterAccount(User user) throws IOException, InterruptedException;
    void saveAccount(AccountEvent accountEvent);
    void updateAccount(AccountEvent accountEvent);
    void deleteAccount(long telegramID);
}

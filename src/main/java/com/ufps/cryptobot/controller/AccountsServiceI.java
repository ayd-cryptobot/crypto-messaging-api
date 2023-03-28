package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.controller.rest.contract.AccountEvent;
import com.ufps.cryptobot.controller.rest.contract.Auth;
import com.ufps.cryptobot.provider.telegram.contract.User;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface AccountsServiceI {
    void callAccountsToRegisterAccount(User user) throws IOException, InterruptedException;

    void saveAccount(AccountEvent accountEvent);

    void updateAccount(AccountEvent accountEvent);

    void deleteAccount(long telegramID);

    boolean authAccount(Auth auth) throws NoSuchAlgorithmException, InvalidKeyException;
}

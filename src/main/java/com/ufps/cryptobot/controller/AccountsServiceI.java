package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.controller.rest.contract.AccountEvent;
import com.ufps.cryptobot.provider.telegram.contract.User;

public interface AccountsServiceI {
    void callAccountsToRegisterAccount(User user);
    void saveAccount(AccountEvent accountEvent);
    void updateAccount(AccountEvent accountEvent);
    void deleteAccount(long telegramID);
}

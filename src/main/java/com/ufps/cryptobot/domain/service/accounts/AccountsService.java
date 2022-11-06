package com.ufps.cryptobot.domain.service.accounts;

import com.sun.jdi.event.ExceptionEvent;
import com.ufps.cryptobot.controller.rest.contract.AccountEvent;
import com.ufps.cryptobot.provider.telegram.contract.User;
import com.ufps.cryptobot.controller.AccountsServiceI;
import com.ufps.cryptobot.domain.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class AccountsService implements AccountsServiceI {

    private UserRepository userRepository;
    private AccountsHTTPRequesterI accountsHTTPRequester;

    public AccountsService(UserRepository userRepository, AccountsHTTPRequesterI accountsHTTPRequester) {
        this.userRepository = userRepository;
        this.accountsHTTPRequester = accountsHTTPRequester;
    }

    @Override
    public void callAccountsToRegisterAccount(User user) throws IOException {
        if (!this.userRepository.existsByTelegramID(user.getId())) {
            this.accountsHTTPRequester.createAccount(user);
        }
    }

    @Override
    public void saveAccount(AccountEvent accountEvent) {
        com.ufps.cryptobot.domain.persistence.entity.User user = new com.ufps.cryptobot.domain.persistence.entity.User(
                accountEvent.getTelegramID(), accountEvent.getFirstName(), accountEvent.getLastName(), accountEvent.getUsername());

        this.userRepository.save(user);
    }

    @Override
    public void updateAccount(AccountEvent accountEvent) {
        this.userRepository.updateFirstNameAndLastNameAndEmailByTelegramID(
                accountEvent.getFirstName(), accountEvent.getLastName(), accountEvent.getEmail(),
                accountEvent.getTelegramID()
        );
    }

    @Override
    public void deleteAccount(long telegramID) {
        this.userRepository.deleteByTelegramID(telegramID);
    }
}

package com.ufps.cryptobot.domain.service.accounts;

import com.sun.jdi.event.ExceptionEvent;
import com.ufps.cryptobot.controller.rest.contract.AccountEvent;
import com.ufps.cryptobot.controller.rest.contract.Auth;
import com.ufps.cryptobot.provider.telegram.contract.User;
import com.ufps.cryptobot.controller.AccountsServiceI;
import com.ufps.cryptobot.domain.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public void callAccountsToRegisterAccount(User user) throws IOException, InterruptedException {
        if (!this.userRepository.existsByTelegramID(user.getId())) {
            this.accountsHTTPRequester.createAccount(user);
        }
    }

    @Override
    public void saveAccount(AccountEvent accountEvent) {
        if (!this.userRepository.existsByTelegramID(accountEvent.getTelegramID())) {
            com.ufps.cryptobot.domain.persistence.entity.User user = new com.ufps.cryptobot.domain.persistence.entity.User(
                    accountEvent.getTelegramID(), accountEvent.getFirstName(), accountEvent.getLastName(), accountEvent.getUsername());

            this.userRepository.save(user);
        }
    }

    @Override
    public void updateAccount(AccountEvent accountEvent) {
        if (!this.userRepository.existsByTelegramID(accountEvent.getTelegramID())) {
            this.saveAccount(accountEvent);

            return;
        }

        this.userRepository.updateFirstNameAndLastNameAndEmailByTelegramID(
                accountEvent.getFirstName(), accountEvent.getLastName(), accountEvent.getEmail(),
                accountEvent.getTelegramID()
        );
    }

    @Override
    public void deleteAccount(long telegramID) {
        this.userRepository.deleteByTelegramID(telegramID);
    }

    @Override
    public boolean authAccount(Auth auth) throws NoSuchAlgorithmException, InvalidKeyException {
        String dataCheckString =
                "auth_date=" + auth.getAuthDate() + "\n" +
                "first_name=" + auth.getFirstName() + "\n" +
                "id=" + auth.getId() + "\n" +
                "username=" + auth.getUsername();

            SecretKeySpec secretKey = new SecretKeySpec(
                    MessageDigest.getInstance("SHA-256").digest("5252956900:AAHoHCzSUpRH6ZLJ8M8kK8OvODMcrNZF5-o".getBytes(StandardCharsets.UTF_8)
                    ), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);

            byte[] result = mac.doFinal(dataCheckString.getBytes(StandardCharsets.UTF_8));

            String resultStr = this.bytesToHex(result);

            if (!auth.getHash().equals(resultStr)) {
                System.out.println("denied  authorization");
                return false;
            }

            System.out.println("auth successfully done");
            return true;
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

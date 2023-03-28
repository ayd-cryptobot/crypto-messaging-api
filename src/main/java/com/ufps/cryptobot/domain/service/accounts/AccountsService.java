package com.ufps.cryptobot.domain.service.accounts;

import com.ufps.cryptobot.controller.rest.contract.AccountEvent;
import com.ufps.cryptobot.controller.rest.contract.Auth;
import com.ufps.cryptobot.provider.telegram.contract.User;
import com.ufps.cryptobot.controller.AccountsServiceI;
import com.ufps.cryptobot.domain.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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


        String secretKey =  sha256("5252956900:AAHoHCzSUpRH6ZLJ8M8kK8OvODMcrNZF5-o");

        if (toHex(hmacSha256(secretKey,dataCheckString)).equals(auth.getHash())) {
            System.out.println("denied  authorization");
            return false;
        }

        System.out.println("auth successfully done");
        return true;
    }

    private String sha256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    private String hmacSha256(String secretKey, String message) throws NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = "HmacSHA256";
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(keySpec);
        byte[] hash = mac.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    private String toHex(String input) {
        char[] hexArray = "0123456789abcdef".toCharArray();
        byte[] bytes = input.getBytes();
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[value >>> 4];
            hexChars[i * 2 + 1] = hexArray[value & 0x0F];
        }
        return new String(hexChars);
    }
}

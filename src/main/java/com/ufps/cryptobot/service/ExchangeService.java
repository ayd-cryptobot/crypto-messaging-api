package com.ufps.cryptobot.service;

import com.ufps.cryptobot.contract.Message;
import com.ufps.cryptobot.contract.Update;
import com.ufps.cryptobot.controller.ExchangeServiceI;
import com.ufps.cryptobot.persistence.entity.Crypto;
import com.ufps.cryptobot.persistence.entity.CryptoUser;
import com.ufps.cryptobot.persistence.entity.User;
import com.ufps.cryptobot.persistence.repository.CryptoRepository;
import com.ufps.cryptobot.persistence.repository.CryptoUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExchangeService implements ExchangeServiceI {

    private final String trackingSuccessfullyConfiguredMessage = "Confirmado! te estaré informando sobre el precio de ";
    private final String deleteTrackingSuggestionMessage = "Si quieres dejar de darle seguimiento a la moneda, ejecuta nuevamente el comando";
    private final String deleteTrackOfACryptoMessage = "¡confirmado! ya no te brindaré seguimiento de la moneda ";

    private MessagingProviderI provider;
    private CryptoUserRepository cryptoUserRepository;
    private CryptoRepository cryptoRepository;

    public ExchangeService(MessagingProviderI provider, CryptoUserRepository cryptoUserRepository, CryptoRepository cryptoRepository) {
        this.provider = provider;
        this.cryptoUserRepository = cryptoUserRepository;
        this.cryptoRepository = cryptoRepository;
    }

    @Override
    public void getCryptoValue(Update update, String crypto, String currency, int nDaysAgo) {
        String message = crypto + " at: " + "40000 " + currency;

        this.sendMessageToUser(update.getMessage().getFrom().getId(), message);
    }

    @Override
    public void getMyCrypto(Update update, String toCurrency) {
        String message = "Currency: " + toCurrency + "\n" +
                "Bitcoin: 40000\n" +
                "Ethereum: 3000\n" +
                "DogeCoin: 0.1\n" +
                "Cardano: 1.2\n" +
                "LiteCoin: 10\n" +
                "BinanceCoin: 500\n" +
                "Tether: 1\n" +
                "Solana:100\n" +
                "BitcoinCash: 5325\n" +
                "Terra: 303";

        this.sendMessageToUser(update.getMessage().getFrom().getId(), message);
    }

    @Override
    public void registerCrypto(User user, String crypto) {
        Optional<CryptoUser> optionalCryptoUser = this.cryptoUserRepository.findByUser_IdAndCrypto_Currency(user.getId(), crypto);
        if (!optionalCryptoUser.isEmpty()) {
            this.cryptoUserRepository.delete(optionalCryptoUser.get());

            this.sendMessageToUser(user.getId(), deleteTrackOfACryptoMessage + crypto);

            return;
        }

        Optional<Crypto> optionalCrypto = this.cryptoRepository.findById(crypto);
        if (optionalCrypto.isEmpty()) {
            //TODO
            return;
        }
        Crypto cryptoCurrency = optionalCrypto.get();

        CryptoUser c = new CryptoUser(user, cryptoCurrency);
        this.cryptoUserRepository.save(c);

        this.sendMessageToUser(user.getId(), trackingSuccessfullyConfiguredMessage + cryptoCurrency.getName());
        this.sendMessageToUser(user.getId(), deleteTrackingSuggestionMessage);
    }

    private void sendMessageToUser(Long userID, String message) {
        Message telegramMessage = new Message(userID, message);
        this.provider.sendMessage(telegramMessage);
    }
}

package com.ufps.cryptobot.service;

import com.ufps.cryptobot.contract.Message;
import com.ufps.cryptobot.contract.Update;
import com.ufps.cryptobot.controller.ExchangeServiceI;
import com.ufps.cryptobot.persistence.entity.Crypto;
import com.ufps.cryptobot.persistence.entity.CryptoUser;
import com.ufps.cryptobot.persistence.entity.User;
import com.ufps.cryptobot.persistence.repository.CryptoRepository;
import com.ufps.cryptobot.persistence.repository.CryptoUserRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeService implements ExchangeServiceI {

    private final String trackingSuccessfullyConfiguredMessage = "Confirmado! te estaré informando sobre el precio de ";
    private final String deleteTrackingSuggestionMessage = "Si quieres dejar de darle seguimiento a la moneda, ejecuta nuevamente el comando";
    private final String deleteTrackOfACryptoMessage = "¡confirmado! ya no te brindaré seguimiento de la moneda ";
    private final String notFollowingAnyCryptoMessage = "No estas haciendo seguimiento a ninguna criptomoneda. Selecciona una de tu interés y te mantendré al día!";

    private MessagingProviderI provider;
    private CryptoUserRepository cryptoUserRepository;
    private CryptoRepository cryptoRepository;
    private PubSubClientI pubSubClient;

    public ExchangeService(MessagingProviderI provider, CryptoUserRepository cryptoUserRepository, CryptoRepository cryptoRepository,
                           PubSubClientI pubSubClient) {
        this.provider = provider;
        this.cryptoUserRepository = cryptoUserRepository;
        this.cryptoRepository = cryptoRepository;
        this.pubSubClient = pubSubClient;
    }

    @Override
    public void getCryptoValue(Update update, String crypto, String currency, int nDaysAgo) throws IOException, InterruptedException {
        JSONArray ja = new JSONArray();
        ja.add(crypto);

        JSONObject obj = new JSONObject();
        obj.put("chat_id", update.getMessage().getFrom().getId());
        obj.put("cryptos", ja);
        obj.put("rango", nDaysAgo);
        String message = obj.toString();

        this.pubSubClient.publishMessage(message, "exchange");
    }

    @Override
    public void getMyCrypto(User user, String toCurrency) throws IOException, InterruptedException {
        List<CryptoUser> currencies = this.cryptoUserRepository.findByUser_Id(user.getId());
        if (currencies.size() == 0) {
            this.sendMessageToUser(user.getId(), notFollowingAnyCryptoMessage);
            return;
        }

        JSONArray ja = new JSONArray();
        for (CryptoUser cu : currencies) {
            ja.add(cu.getCrypto().getName());
        }

        JSONObject obj = new JSONObject();
        obj.put("chat_id", user.getId());
        obj.put("cryptos", ja);
        obj.put("rango", 0);
        String message = obj.toString();

        this.pubSubClient.publishMessage(message, "exchange");
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

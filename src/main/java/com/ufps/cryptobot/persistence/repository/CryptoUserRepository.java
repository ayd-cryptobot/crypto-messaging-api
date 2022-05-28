package com.ufps.cryptobot.persistence.repository;

import com.ufps.cryptobot.persistence.entity.Crypto;
import com.ufps.cryptobot.persistence.entity.CryptoUser;
import com.ufps.cryptobot.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CryptoUserRepository extends CrudRepository<CryptoUser, Integer> {
    Optional<CryptoUser> findByUser_IdAndCrypto_Currency(Long id, String currency);
}

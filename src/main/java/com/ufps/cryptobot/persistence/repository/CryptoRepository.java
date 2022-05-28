package com.ufps.cryptobot.persistence.repository;

import com.ufps.cryptobot.persistence.entity.Crypto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoRepository extends CrudRepository<Crypto, String> {
}

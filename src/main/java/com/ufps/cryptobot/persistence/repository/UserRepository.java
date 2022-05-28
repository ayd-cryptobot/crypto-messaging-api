package com.ufps.cryptobot.persistence.repository;

import com.ufps.cryptobot.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}

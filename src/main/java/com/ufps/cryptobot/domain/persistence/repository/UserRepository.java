package com.ufps.cryptobot.domain.persistence.repository;

import com.ufps.cryptobot.domain.persistence.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("update User u set u.firstName = ?1, u.lastName = ?2, u.email = ?3 where u.telegramID = ?4")
    void updateFirstNameAndLastNameAndEmailByTelegramID(String firstName, String lastName, String email, Long telegramID);

    @Transactional
    long deleteByTelegramID(Long telegramID);

    boolean existsByTelegramID(Long telegramID);

    List<User> findAll();
}

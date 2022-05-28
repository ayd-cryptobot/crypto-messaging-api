package com.ufps.cryptobot.service;

import com.ufps.cryptobot.contract.User;
import com.ufps.cryptobot.controller.UsersServiceI;
import com.ufps.cryptobot.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService implements UsersServiceI {

    private UserRepository userRepository;

    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public com.ufps.cryptobot.persistence.entity.User findUser(User user) {
        Optional<com.ufps.cryptobot.persistence.entity.User> optionalUser = this.userRepository.findById(user.getId());
        if (optionalUser.isEmpty()) {
            this.saveUser(user);
        }

        return optionalUser.get();
    }

    private void saveUser(User user) {
        com.ufps.cryptobot.persistence.entity.User u = new com.ufps.cryptobot.persistence.entity.User(user.getId(),
                user.getFirst_name(), user.getLast_name(), user.getUsername());

        this.userRepository.save(u);
    }
}

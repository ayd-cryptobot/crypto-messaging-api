package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.contract.User;

public interface UsersServiceI {
    com.ufps.cryptobot.persistence.entity.User findUser(User user);
}

package com.ufps.cryptobot.controller.rest.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Auth {
    private String id;
    private String firstName;
    private String username;
    private String authDate;
    private String hash;
}

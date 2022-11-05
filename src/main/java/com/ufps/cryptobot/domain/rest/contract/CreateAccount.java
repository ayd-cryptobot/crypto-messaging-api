package com.ufps.cryptobot.domain.rest.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateAccount {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String rol;
}

package com.ufps.cryptobot.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Crypto {

    @Id
    private String currency;

    @Column
    private String name;

    @OneToMany(mappedBy = "crypto", cascade = CascadeType.ALL)
    private List<CryptoUser> cryptoUsers;
}

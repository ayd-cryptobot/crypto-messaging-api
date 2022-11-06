package com.ufps.cryptobot.domain.rest.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CryptoPrice {
    private String date;
    private int price;
}

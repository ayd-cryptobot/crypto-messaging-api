package com.ufps.cryptobot.contract.exchange;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Crypto {
    @JsonAlias("nombre")
    private String name;

    @JsonAlias("history")
    private List<Double> history;
}

package com.ufps.cryptobot.domain.rest.contract.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TextGeneratedResponse {
    @JsonAlias("result")
    private String result;
}

package com.ufps.cryptobot.controller.rest.contract;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountEvent {
    @JsonAlias({"telegram_user_id"})
    @JsonProperty(required = true)
    private Long telegramID;

    @JsonAlias("operation_type")
    @JsonProperty(required = true)
    private String operationType;

    @JsonAlias("first_name")
    @JsonProperty()
    private String firstName;

    @JsonAlias("last_name")
    @JsonProperty()
    private String lastName;

    @JsonAlias("email")
    @JsonProperty()
    private String email;

    @JsonAlias("username")
    @JsonProperty()
    private String username;
}

package com.ufps.cryptobot.controller.rest.contract;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiffuseMessage {
    @JsonAlias("title")
    @NotEmpty(message = "Empty title")
    private String title;

    @JsonAlias("body")
    @NotEmpty(message = "Empty body")
    private String body;

    @JsonAlias("image_link")
    @NotEmpty(message = "Empty image_link")
    private String imageLink;
}

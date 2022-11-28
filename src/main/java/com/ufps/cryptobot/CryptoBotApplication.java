package com.ufps.cryptobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CryptoBotApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CryptoBotApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(CryptoBotApplication.class);
    }
}

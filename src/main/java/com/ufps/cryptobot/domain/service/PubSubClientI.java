package com.ufps.cryptobot.domain.service;

import java.io.IOException;

public interface PubSubClientI {
    void publishMessage(String message, String topic) throws IOException, InterruptedException;
}

package com.ufps.cryptobot.service;

import java.io.IOException;

public interface PubSubClientI {
    void publishMessage(String message) throws IOException, InterruptedException;
}

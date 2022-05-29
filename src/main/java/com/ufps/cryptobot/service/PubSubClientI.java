package com.ufps.cryptobot.service;

import java.io.IOException;
import java.util.Map;

public interface PubSubClientI {
    void publishMessage(String message, String topic) throws IOException, InterruptedException;
}

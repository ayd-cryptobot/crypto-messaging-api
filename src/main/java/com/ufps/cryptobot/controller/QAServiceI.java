package com.ufps.cryptobot.controller;

import com.ufps.cryptobot.provider.telegram.contract.Message;

import java.io.IOException;

public interface QAServiceI {
    public void sendQuestionToAI(Message message) throws IOException, InterruptedException;
}

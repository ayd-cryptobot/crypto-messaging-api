package com.ufps.cryptobot.domain.service.qa;

import com.ufps.cryptobot.controller.QAServiceI;
import com.ufps.cryptobot.domain.rest.contract.query.QueryGenerateText;
import com.ufps.cryptobot.domain.rest.contract.response.TextGeneratedResponse;
import com.ufps.cryptobot.domain.service.messaging.MessagingProviderI;
import com.ufps.cryptobot.provider.telegram.contract.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class QAService implements QAServiceI {

    private final MessagingProviderI messagingProvider;
    private final QAHTTPRequesterI qaHTTPRequester;

    public QAService(MessagingProviderI messagingProvider, QAHTTPRequesterI qaHTTPRequester) {
        this.messagingProvider = messagingProvider;
        this.qaHTTPRequester = qaHTTPRequester;
    }

    @Override
    public void sendQuestionToAI(Message message) throws IOException, InterruptedException {
        QueryGenerateText queryGenerateText = new QueryGenerateText(message.getText());

        TextGeneratedResponse textGeneratedResponse = this.qaHTTPRequester.requestGenerateText(queryGenerateText);

        message.setText(textGeneratedResponse.getResult());

        this.messagingProvider.sendMessage(message, null);
    }
}

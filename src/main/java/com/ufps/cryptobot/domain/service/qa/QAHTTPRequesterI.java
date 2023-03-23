package com.ufps.cryptobot.domain.service.qa;


import com.ufps.cryptobot.domain.rest.contract.query.QueryGenerateText;
import com.ufps.cryptobot.domain.rest.contract.response.TextGeneratedResponse;

import java.io.IOException;

public interface QAHTTPRequesterI {
    public TextGeneratedResponse requestGenerateText(QueryGenerateText queryGenerateText) throws IOException, InterruptedException;
}

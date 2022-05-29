package com.ufps.cryptobot.pubsub;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import com.ufps.cryptobot.service.PubSubClientI;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class Client implements PubSubClientI {
    private String projectID;
    private String newsTopicID;
    private TopicName newsTopicName;
    private String exchangeTopicID;
    private TopicName exchangeTopicName;

    public Client() {
        this.projectID = System.getenv("PROJECT_ID");
        this.newsTopicID = System.getenv("NEWS_TOPIC_ID");
        this.newsTopicName = TopicName.of(this.projectID, this.newsTopicID);
        this.exchangeTopicID = System.getenv("EXCHANGE_TOPIC_ID");
        this.exchangeTopicName = TopicName.of(this.projectID, this.exchangeTopicID);
    }

    public void publishMessage(String message, String topic) throws IOException, InterruptedException {
        Publisher publisher = null;
        try {
            if (topic == "news") {
                publisher = Publisher.newBuilder(newsTopicName).build();
            } else {
                publisher = Publisher.newBuilder(exchangeTopicName).build();
            }

            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

            ApiFuture<String> future = publisher.publish(pubsubMessage);

            ApiFutures.addCallback(future, new ApiFutureCallback<String>() {

                        @Override
                        public void onFailure(Throwable throwable) {
                            if (throwable instanceof ApiException) {
                                ApiException apiException = ((ApiException) throwable);
                                System.out.println(apiException.getStatusCode().getCode());
                                System.out.println(apiException.isRetryable());
                                throw apiException;
                            }
                            System.out.println("Error publishing message : " + message);
                        }

                        @Override
                        public void onSuccess(String messageId) {
                            System.out.println("Published message ID: " + messageId);
                        }
                    },
                    MoreExecutors.directExecutor());
        } finally {
            if (publisher != null) {
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }
}

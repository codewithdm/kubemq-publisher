package com.codewithdm.kubemq.publisher;

import io.grpc.stub.StreamObserver;
import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.event.*;
import io.kubemq.sdk.queue.Queue;
import io.kubemq.sdk.queue.Transaction;
import io.kubemq.sdk.queue.TransactionMessagesResponse;
import io.kubemq.sdk.subscription.SubscribeRequest;
import io.kubemq.sdk.subscription.SubscribeType;
import io.kubemq.sdk.tools.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLException;
import java.io.IOException;

@Component
public class PublisherEvent {
    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private Channel channel;
    @Autowired
    private Queue queue;
    @PostConstruct
    public void listen() throws IOException, ServerAddressNotSuppliedException {
        taskExecutor.execute(() -> {
            while (true) {
                try {
                    Transaction transaction = queue.CreateTransaction();
                    TransactionMessagesResponse response = transaction.Receive(10, 10);
                    if (response.getMessage().getBody().length > 0) {
                        String message = (String)Converter.FromByteArray(response.getMessage().getBody());
                        System.out.println("message: "+message);
                            transaction.AckMessage();
                            Event event = new Event();
                            event.setEventId(response.getMessage().getMessageID());
                            event.setBody(Converter.ToByteArray(message));
                            System.out.println("Sending event: id= "+ event.getEventId());
                            channel.SendEvent(event);
                    } else {
                        System.out.println("No messages");
                    }
                    Thread.sleep(10000);
                } catch (Exception e) {
                    System.out.println("error");
                }
            }
        });
//        System.out.println("post construct......");
//        String ChannelName = "testing_event_channel", ClientID = "hello-world-sender",
//                KubeMQServerAddress = "localhost:50000";
//
//            io.kubemq.sdk.event.Channel channel = new io.kubemq.sdk.event.Channel(ChannelName, queue.getClientID(), false,
//                queue.getServerAddress());
//        Event event = new Event();
//        event.setBody(Converter.ToByteArray("hello kubemq - sending single event"));
//        Result result;
//        try {
//            result = channel.SendEvent(event);
//            if (!result.isSent()) {
//                System.out.println("Could not send single message");
//            }
//        } catch (ServerAddressNotSuppliedException e) {
//            System.out.printf("Could not send single message: %s", e.getMessage());
//            e.printStackTrace();
//        }
    }

}

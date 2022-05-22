package com.codewithdm.kubemq.publisher;

import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.queue.Message;
import io.kubemq.sdk.queue.Queue;
import io.kubemq.sdk.queue.ReceiveMessagesResponse;
import io.kubemq.sdk.queue.SendMessageResult;
import io.kubemq.sdk.tools.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping(("/publisher"))
public class PublisherController {

    @Autowired
    private Queue queue;
    @GetMapping("/send/{message}")
    public SendMessageResult sendMessageToQueue(@PathVariable String message) throws ServerAddressNotSuppliedException, IOException {
        SendMessageResult resSend = queue.SendQueueMessage(new Message()
                .setBody(Converter.ToByteArray(message))
                .setMetadata("someMeta"));
        if (resSend.getIsError()) {
            System.out.printf("Message enqueue error, error: %s", resSend.getError());
        }
        return resSend;
    }

}

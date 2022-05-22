package com.codewithdm.kubemq.publisher;

import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.event.Channel;
import io.kubemq.sdk.queue.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLException;

@SpringBootApplication
public class KubemqPublisherApplication {

	public static void main(String[] args) {
		SpringApplication.run(KubemqPublisherApplication.class, args);
	}

	@Bean
	public Queue queue() throws ServerAddressNotSuppliedException, SSLException {
		return new Queue("my-queue", "hello-world-sender", "localhost:50000");
	}

	@Bean
	public Channel channel() {
		return new Channel("transactions", "hello-world-sender", true, "localhost:50000");
	}
}

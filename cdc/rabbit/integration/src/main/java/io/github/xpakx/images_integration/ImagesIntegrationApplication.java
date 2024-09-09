package io.github.xpakx.images_integration;

import io.github.xpakx.images_integration.transformation.StringToEventTransformer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.amqp.dsl.Amqp;

@SpringBootApplication
public class ImagesIntegrationApplication {
	@Autowired
	StringToEventTransformer stringToEventTransformer;

	public static void main(String[] args) {
		SpringApplication.run(ImagesIntegrationApplication.class, args);
	}

	@Bean
	IntegrationFlow handle(ConnectionFactory connectionFactory, @Value("${rabbitmq.queue}") String queueName) {
		return IntegrationFlow
				.from(Amqp.inboundAdapter(connectionFactory, queueName))
				.transform(Transformers.objectToString())
				.transform(stringToEventTransformer)
				.handle((payload, headers) -> {
					System.out.println(payload.getClass().getName());
					System.out.println(payload);
					return null;
				})
				.get();
	}

}
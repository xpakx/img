package io.github.xpakx.images_integration;

import io.github.xpakx.images_integration.config.CustomAmqpHeaderMapper;
import io.github.xpakx.images_integration.transformation.StringToEventTransformer;
import io.github.xpakx.images_integration.transformation.model.Event;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.QueueChannelSpec;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.support.MessageBuilder;


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
				.channel("queueChannel")
				.get();
	}

	@Bean
	public QueueChannelSpec queueChannel() {
		return MessageChannels.queue();
	}

	@Bean
	IntegrationFlow publish(@Value("${rabbitmq.exchange.out}") String resultExchange, AmqpTemplate template) {
		return IntegrationFlow
				.from("queueChannel")
				.transform(Event.class,
						event -> {
							return MessageBuilder.withPayload(event)
									.setHeader("routingKey", event.tableName())
									.build();
						}
				)
				.transform(Transformers.toJson())
				.handle(
						Amqp.outboundAdapter(template)
								.exchangeName(resultExchange)
								.routingKeyFunction(m-> m.getHeaders().get("routingKey", String.class))
								.headerMapper(new CustomAmqpHeaderMapper())
				)
				.get();
	}
}

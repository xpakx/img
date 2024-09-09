package io.github.xpakx.images_integration.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfig {
    @Bean
    Exchange changeExchange(@Value("${rabbitmq.exchange}") String changeExchangeName) {
        return ExchangeBuilder.topicExchange(changeExchangeName)
                .durable(true)
                .build();
    }

    @Bean
    public Queue changeQueue(@Value("${rabbitmq.queue}") String changeQueueName) {
        return QueueBuilder.durable(changeQueueName)
                .build();
    }

    @Bean
    Binding worksBinding(Queue changeQueue, Exchange changeExchange) {
        return BindingBuilder
                .bind(changeQueue)
                .to(changeExchange).with("change").noargs();
    }

    @Bean
    Exchange eventExchange(@Value("${rabbitmq.exchange.out}") String eventExchangeName) {
        return ExchangeBuilder.topicExchange(eventExchangeName)
                .durable(true)
                .build();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}

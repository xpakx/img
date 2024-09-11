package io.github.xpakx.images_cdc.rabbit;

import io.github.xpakx.images_cdc.data.EventService;
import io.github.xpakx.images_cdc.debezium.model.Account;
import io.github.xpakx.images_cdc.debezium.model.Image;
import io.github.xpakx.images_cdc.debezium.model.Value;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("rabbit")
public class AccountEventListener {
    private final EventService service;

    public AccountEventListener(EventService service) {
        this.service = service;
    }

    @RabbitListener(queues = "${amqp.queue.account}")
    void handleAccount(final Value<Account> event) {
        try {
            service.saveUser(event);
        } catch (final Exception e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    @RabbitListener(queues = "${amqp.queue.image}")
    void handleImage(final Value<Image> event) {
        try {
            service.saveImage(event);
        } catch (final Exception e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}

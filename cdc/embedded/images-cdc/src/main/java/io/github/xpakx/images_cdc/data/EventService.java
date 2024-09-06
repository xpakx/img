package io.github.xpakx.images_cdc.data;

import io.github.xpakx.images_cdc.debezium.model.Account;
import io.github.xpakx.images_cdc.debezium.model.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventService {
    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public void saveUser(Value<Account> user) {
        var event = new Event(UUID.randomUUID(), "created");
        repository.save(event);
    }
}

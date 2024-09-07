package io.github.xpakx.images_cdc.data;

import io.github.xpakx.images_cdc.data.dto.EventData;
import io.github.xpakx.images_cdc.debezium.model.Account;
import io.github.xpakx.images_cdc.debezium.model.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class EventService {
    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public void saveUser(Value<Account> user) {
        var event = new Event(UUID.randomUUID(), "created", user.after().id());
        repository.save(event);
    }

    public List<EventData> getEvents(Long userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(this::toData)
                .toList();
    }

    public List<EventData> getAllEvents() {
        List<EventData> result = new ArrayList<>();

        StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::toData)
                .forEach(result::add);
        return result;
    }

    private EventData toData(Event event) {
        return new EventData(event.getId(), event.getName(), event.getUserId());
    }
}

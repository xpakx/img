package io.github.xpakx.images_cdc.data;

import io.github.xpakx.images_cdc.data.dto.EventData;
import io.github.xpakx.images_cdc.debezium.model.Account;
import io.github.xpakx.images_cdc.debezium.model.Image;
import io.github.xpakx.images_cdc.debezium.model.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class EventService {
    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public void saveUser(Value<Account> user) {
        switch (user.operation()) {
            case Create ->
                    repository.save(createUserEvent(user.after(), "account_created"));
            case Delete ->
                    repository.save(createUserEvent(user.before(), "account_deleted"));
            case Update ->
                    repository.save(createUserEvent(user.after(), "avatar_changed"));
            default -> {}
        }
    }

    private Event createUserEvent(Account user, String eventName) {
        return new Event(
                UUID.randomUUID(),
                eventName,
                user.id(),
                Instant.now()
        );
    }

    public void saveImage(Value<Image> image) {
        switch (image.operation()) {
            case Create ->
                    repository.save(createImageEvent(image.after(), "image_created"));
            case Delete ->
                    repository.save(createImageEvent(image.before(), "image_deleted"));
            case Update ->
                    repository.save(createImageEvent(image.after(), "caption_changed"));
            default -> {}
        }
    }

    private Event createImageEvent(Image image, String eventName) {
        return new Event(
                UUID.randomUUID(),
                eventName,
                image.userId(),
                Instant.now()
        );
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

    private boolean avatarChanged(Value<Account> user) {
        Objects.requireNonNull(user.after());
        Objects.requireNonNull(user.before());
        return !user.after().avatarUrl().equals(user.before().avatarUrl());
    }

    private boolean captionChanged(Value<Image> image) {
        Objects.requireNonNull(image.after());
        Objects.requireNonNull(image.before());
        return !image.after().caption().equals(image.before().caption());
    }
}

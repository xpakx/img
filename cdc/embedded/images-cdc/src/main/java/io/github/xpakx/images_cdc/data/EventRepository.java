package io.github.xpakx.images_cdc.data;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EventRepository extends CrudRepository<Event, UUID> {
}

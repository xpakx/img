package io.github.xpakx.images_cdc.debezium;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.config.Configuration;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import io.github.xpakx.images_cdc.data.Event;
import io.github.xpakx.images_cdc.data.EventService;
import io.github.xpakx.images_cdc.debezium.model.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
@Profile("embedded")
public class DebeziumListener {
    private final Executor executor;
    private final DebeziumEngine<ChangeEvent<String, String>> debeziumEngine;
    private final EventService service;

    public DebeziumListener(Configuration postgresConnector, EventService service) {
        this.executor = Executors.newSingleThreadExecutor();

        this.debeziumEngine = DebeziumEngine.create(Json.class)
                .using(postgresConnector.asProperties())
                .notifying(this::handleEvent)
                .build();

        this.service = service;
    }

    private void handleEvent(ChangeEvent<String, String> event) {
        var keyOpt = parseKey(event.key());
        if (keyOpt.isEmpty()) {
            System.out.println("Unknown key: " + event.key());
            return;
        }
        var key = keyOpt.get();

        System.out.println("Key: " + key);
        var valueOpt = parseValue(event.value(), key.table().getType());
        System.out.println("Value: " + valueOpt);
        switch (key.table()) {
            case Account -> service.saveUser((Value<Account>) valueOpt.get());
            case Image -> service.saveImage((Value<Image>) valueOpt.get());
        }
    }

    private Optional<Key> parseKey(String key) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode keyNode = objectMapper.readTree(key);
            String tableName = keyNode.path("schema").path("name").asText();
            Optional<TableName> table = TableName.toName(tableName);
            if (table.isEmpty()) {
                return Optional.empty();
            }

            Long id = keyNode.path("payload").path("id").asLong();
            return Optional.of(new Key(table.get(), id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private <T> Optional<Value<T>> parseValue(String value, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode valueNode = objectMapper.readTree(value);
            JsonNode payloadNode = valueNode.path("payload");

            T before = objectMapper
                    .treeToValue(payloadNode.path("before"), clazz);
            T after = objectMapper
                    .treeToValue(payloadNode.path("after"), clazz);

            return Optional.of(new Value<>(before, after));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @PostConstruct
    private void start() {
        this.executor.execute(debeziumEngine);
    }

    @PreDestroy
    private void stop() throws IOException {
        if (this.debeziumEngine != null) {
            this.debeziumEngine.close();
        }
    }

}

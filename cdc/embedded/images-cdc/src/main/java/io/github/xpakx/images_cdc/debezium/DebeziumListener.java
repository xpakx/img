package io.github.xpakx.images_cdc.debezium;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.config.Configuration;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import io.github.xpakx.images_cdc.debezium.model.Key;
import io.github.xpakx.images_cdc.debezium.model.TableName;
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

    public DebeziumListener(Configuration postgresConnector) {
        this.executor = Executors.newSingleThreadExecutor();

        this.debeziumEngine = DebeziumEngine.create(Json.class)
                .using(postgresConnector.asProperties())
                .notifying(this::handleEvent)
                .build();
    }

    private void handleEvent(ChangeEvent<String, String> event) {
        var keyOpt = parseKey(event.key());
        if (keyOpt.isEmpty()) {
            System.out.println("Unknown key: " + event.key());
            return;
        }
        var key = keyOpt.get();

        System.out.println("Key: " + key);
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

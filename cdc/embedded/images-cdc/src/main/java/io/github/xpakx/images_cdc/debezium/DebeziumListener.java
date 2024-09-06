package io.github.xpakx.images_cdc.debezium;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.config.Configuration;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        //insert, update, delete
        System.out.println("Key: " + event.key());
        System.out.println("Value: " + event.value());

        ObjectMapper objectMapper = new ObjectMapper();
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

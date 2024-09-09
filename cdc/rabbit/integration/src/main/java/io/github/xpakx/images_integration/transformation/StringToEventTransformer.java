package io.github.xpakx.images_integration.transformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xpakx.images_integration.transformation.model.Account;
import io.github.xpakx.images_integration.transformation.model.Event;
import io.github.xpakx.images_integration.transformation.model.Image;
import org.springframework.integration.transformer.AbstractPayloadTransformer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StringToEventTransformer extends AbstractPayloadTransformer<String, Event<?>> {

    @Override
    protected Event<?> transformPayload(String event) {
        System.out.println(event);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode value = objectMapper.readTree(event);
            String schemaName = value
                    .path("schema")
                    .path("name")
                    .asText();
            System.out.println("Name: "  + schemaName);
            var clazzOpt = schemaNameToClass(schemaName);
            if (clazzOpt.isEmpty()) {
                return null;
            }
            var clazz = clazzOpt.get();
            JsonNode payloadNode = value.path("payload");

            System.out.println(payloadNode.toString());
            var before = objectMapper
                    .treeToValue(payloadNode.path("before"), clazz);
            var after = objectMapper
                    .treeToValue(payloadNode.path("after"), clazz);
            return new Event<>(before, after, schemaNameToRoutingKey(schemaName));
        } catch (Exception e) {
            System.out.println("Cannot parse");
        }
        return null;
    }

    private Optional<Class<?>> schemaNameToClass(String schemaName) {
        return switch (schemaName) {
            case "postgres.public.account.Envelope" -> Optional.of(Account.class);
            case "postgres.public.image.Envelope" -> Optional.of(Image.class);
            default -> Optional.empty();
        };
    }
    private String schemaNameToRoutingKey(String schemaName) {
        return switch (schemaName) {
            case "postgres.public.account.Envelope" -> "account";
            case "postgres.public.image.Envelope" -> "image";
            default -> "empty";
        };
    }
}

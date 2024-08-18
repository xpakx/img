package io.github.xpakx.images.config.sqids;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sqids.Sqids;

@Configuration
public class SqidsConfig {

    @Bean
    public Sqids sqids() {
        return Sqids.builder().build();
    }
}

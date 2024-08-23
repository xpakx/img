package io.github.xpakx.images;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ImagesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImagesApplication.class, args);
	}

}

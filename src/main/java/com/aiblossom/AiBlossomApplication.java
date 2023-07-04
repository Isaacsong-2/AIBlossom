package com.aiblossom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AiBlossomApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiBlossomApplication.class, args);
    }

}

package com.buizzment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class BuizzmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(BuizzmentApplication.class, args);
    }
}
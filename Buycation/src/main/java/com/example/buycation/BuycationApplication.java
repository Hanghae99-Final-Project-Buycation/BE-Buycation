package com.example.buycation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BuycationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuycationApplication.class, args);
    }

}

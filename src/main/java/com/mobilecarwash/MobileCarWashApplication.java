package com.mobilecarwash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MobileCarWashApplication {
    public static void main(String[] args) {
        SpringApplication.run(MobileCarWashApplication.class, args);
    }
}

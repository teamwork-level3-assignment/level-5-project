package com.sparta.lv3assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
public class Lv3AssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lv3AssignmentApplication.class, args);
    }

}

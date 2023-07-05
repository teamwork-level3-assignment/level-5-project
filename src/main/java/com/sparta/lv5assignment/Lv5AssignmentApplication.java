package com.sparta.lv5assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Lv5AssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lv5AssignmentApplication.class, args);
    }

}

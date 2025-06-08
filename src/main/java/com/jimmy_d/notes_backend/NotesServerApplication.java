package com.jimmy_d.notes_backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan
public class NotesServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesServerApplication.class, args);
        log.info("NotesServerApplication started successfully.");
    }

}

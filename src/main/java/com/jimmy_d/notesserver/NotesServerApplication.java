package com.jimmy_d.notesserver;

import com.jimmy_d.notesserver.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan
public class NotesServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesServerApplication.class, args);

    }

}

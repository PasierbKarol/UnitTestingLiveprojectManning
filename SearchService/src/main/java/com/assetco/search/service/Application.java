package com.assetco.search.service;

import org.slf4j.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

import javax.annotation.*;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void onStarted() {

        LoggerFactory.getLogger(Application.class).info("Started...");
    }
}

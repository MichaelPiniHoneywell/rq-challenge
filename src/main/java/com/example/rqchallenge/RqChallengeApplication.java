package com.example.rqchallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RqChallengeApplication {
    private static final Logger logger = LoggerFactory.getLogger(RqChallengeApplication.class);

    public static void main(String[] args) {
        logger.info("Starting application....");
        SpringApplication.run(RqChallengeApplication.class, args);
    }

}

package com.quizplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the online quiz platform application.
 *
 * <p>This class starts Spring Boot and initializes the application's
 * controllers, repositories, and services.</p>
 */
@SpringBootApplication
public class QuizPlatformApplication {

    /** Launches the Spring Boot application with the provided command-line arguments. */
    public static void main(String[] args) { SpringApplication.run(QuizPlatformApplication.class, args); }
}

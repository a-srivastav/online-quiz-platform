package com.quizplatform.config;

import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** Registers shared Spring beans, including the encoder used for password hashing. */
@Configuration public class AppConfig { /** Creates the BCrypt password encoder shared by application services. */
 @Bean public BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();} }

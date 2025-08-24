package com.example.securityskilltesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching
public class SecuritySkillTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecuritySkillTestingApplication.class, args);
    }

}

package com.erm.legacy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * ERM Complexity Demo — miniaturized stand-in for a 15+ year, ~2.2M LOC estate.
 *
 * LEGACY PROBLEM: Hybrid monolithic + microservices boot path.
 * Original production: EAR on WebLogic (acquired RiskGuard 2012) coexists with
 * Spring Boot sidecars for newer vendor-risk APIs. This single WAR models that split.
 *
 * Tech stack (per Technical & Business Complexity table):
 *   Backend  — Java 8, Spring MVC
 *   Frontend — JSP, AngularJS
 *   Database — Oracle + PostgreSQL (H2 profile for local demo)
 */
@SpringBootApplication
public class ErmComplexityApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ErmComplexityApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ErmComplexityApplication.class, args);
    }
}

package com.masterschool.admissions.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterschool.admissions.builder.AdmissionsSystemBuilder;
import com.masterschool.admissions.facade.AdmissionsFacade;

import com.masterschool.admissions.service.TaskRequestMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for wiring application components.
 *
 * Bridges the manual builder (AdmissionsSystemBuilder)
 * with Spring's dependency injection.
 */
@Configuration
public class AppConfig {

    @Bean
    public AdmissionsFacade admissionsFacade() {
        return AdmissionsSystemBuilder.build();
    }


    @Bean
    public TaskRequestMapper taskRequestMapper(ObjectMapper objectMapper) {
        return new TaskRequestMapper(objectMapper);
    }
}
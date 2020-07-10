package com.example.store.config;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
public class CoreConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    Logger logger(InjectionPoint ip) {
        return LoggerFactory.getLogger(ip.getMember().getDeclaringClass());
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

}

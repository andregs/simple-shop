package com.example.store.user;

import com.example.store.config.CoreConfiguration;
import com.example.store.config.SecurityConfiguration;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan
@Import({CoreConfiguration.class, SecurityConfiguration.class})
public class UserConfiguration {
 

}
package com.spaceshare.backend.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:env.properties")
public class AppConfig {
	
}

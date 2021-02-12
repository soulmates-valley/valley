package com.soulmates.valley.common.config;

import org.neo4j.cypherdsl.core.renderer.Renderer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Renderer renderer() {
        return Renderer.getDefaultRenderer();
    }
}

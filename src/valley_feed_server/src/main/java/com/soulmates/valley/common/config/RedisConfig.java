package com.soulmates.valley.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulmates.valley.feature.posting.dto.PostInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String redisHostName;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHostName, redisPort);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, PostInfo> redisTemplate(JedisConnectionFactory jedisConnectionFactory, ObjectMapper objectMapper) {
        final RedisTemplate<String, PostInfo> redisTemplate = new RedisTemplate<>();
        var serializer = new Jackson2JsonRedisSerializer<>(PostInfo.class);
        serializer.setObjectMapper(objectMapper);
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setDefaultSerializer(serializer);
        return redisTemplate;
    }
}

package com.soulmates.valley;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableConfigurationProperties
@EnableNeo4jRepositories("com.soulmates.valley.domain.repository.graph")
@EnableMongoRepositories("com.soulmates.valley.domain.repository.doc")
public class ValleyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ValleyApplication.class, args);
    }
}

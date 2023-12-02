package com.rateflow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories("com.rateflow")
@Configuration
public class MongoConfig {}

package com.buizzment.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    
    @Value("${spring.data.mongodb.uri:mongodb://localhost:27017/buizzment}")
    private String mongoUri;
    
    @Value("${spring.data.mongodb.database:buizzment}")
    private String databaseName;

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new StringToYearMonthConverter(),
                new YearMonthToStringConverter()
        ));
    }

    
    @Override
    protected String getDatabaseName() {
        return databaseName;
    }
    
    @Override
    @Bean
    public MongoClient mongoClient() {
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
            CodecRegistries.fromCodecs(new YearMonthCodec()),
            MongoClientSettings.getDefaultCodecRegistry()
        );
        
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(mongoUri))
            .codecRegistry(codecRegistry)
            .build();
        
        return MongoClients.create(settings);
    }
    
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
} 
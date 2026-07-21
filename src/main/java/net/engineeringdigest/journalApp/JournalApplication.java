package net.engineeringdigest.journalApp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


// @SpringBootApplication does the job of these three annotations:
// @Configuration, @EnableAutoConfiguration, @ComponentScan(Will scan only in this package)

// Rest API -> Represent State Transfer (RestController) = HTTP Verb + URL = IP + Port + Url

@SpringBootApplication
@EnableTransactionManagement
public class JournalApplication {

    public static void main(String[] args) {
        SpringApplication.run(JournalApplication.class, args);
    }

    @Bean
    public PlatformTransactionManager add(MongoDatabaseFactory dbFactory) {
        // MongoDb already has a MongoDatabaseFactory bean, it passes that into this method, we do not need to createUser a new MongoDatabaseFactory.
        return new MongoTransactionManager(dbFactory);
    }

    /*
    PlatformTransactionManager - Interface
    MongoTransactionManager - Implementation Class
     */

}
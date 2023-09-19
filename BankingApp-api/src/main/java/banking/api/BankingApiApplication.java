package banking.api;

import bankingservice.service.ServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

/**
 * A Configuration class that can declare one or more @Bean methods and trigger auto-configuration and component scanning.
 * This class launches a Spring Application from Java main method.
 */
@SpringBootApplication
@EntityScan("bankingservice.service.entity")
@Import(ServiceConfiguration.class)
public class BankingApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankingApiApplication.class, args);
    }
}


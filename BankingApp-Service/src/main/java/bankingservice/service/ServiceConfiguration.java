package bankingservice.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Enabling the component scan and entity scan of classes in the below mentioned "com.upgrad.FoodOrderingApp.service" and "com.upgrad.FoodOrderingApp.service.entity" packages respectively.
 */
@Configuration
@ComponentScan("bankingservice.service")
public class ServiceConfiguration {
}

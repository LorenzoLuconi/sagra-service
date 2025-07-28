package it.loreluc.sagraservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration
public class TestContainerConfig {

    // Copiato da qui: https://github.com/spring-projects/spring-boot/issues/34905
    // Migliora, ma non riesco ad usare un unico MySQL per tutti i test
    @Bean
    @ServiceConnection
    public MySQLContainer<?> mySQLContainer() {
        return new MySQLContainer<>("mysql:8").withDatabaseName("sagra_test");
    }
}

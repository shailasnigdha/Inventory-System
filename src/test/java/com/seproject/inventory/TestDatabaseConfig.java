package com.seproject.inventory;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@TestConfiguration
public class TestDatabaseConfig {

    @Bean
    @Primary
    public DataSource testDataSource() {
        try {
            return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .setName("inventorydb")
                    .build();
        } catch (IllegalStateException e) {
            if (e.getMessage() != null && e.getMessage().contains("Driver for test database type [H2] is not available")) {
                throw new IllegalStateException(
                    "H2 database driver is not available on the classpath. " +
                    "Make sure to:\n" +
                    "1. Run 'mvn clean install' to download dependencies\n" +
                    "2. In IntelliJ: Right-click project → Maven → Reload Projects\n" +
                    "3. Or go to File → Invalidate Caches → Invalidate and Restart",
                    e
                );
            }
            throw e;
        }
    }
}




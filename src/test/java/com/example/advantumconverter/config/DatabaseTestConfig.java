package com.example.advantumconverter.config;

import lombok.experimental.UtilityClass;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.util.Map;

@TestConfiguration
public class DatabaseTestConfig {

    @UtilityClass
    public static class MySql {

        public final String IMAGE = "mysql:8.0";

        public final String ROOT_USERNAME = "root";
        public final String USERNAME = "user";
        public final String PASSWORD = "test";
        public final String DATABASE = "advantum";
    }

    private static final MySQLContainer<?> DATABASE_CONTAINER = new MySQLContainer<>(MySql.IMAGE);

    @Primary
    @Bean
    public DataSource testDataSource() {
        DATABASE_CONTAINER
                .withTmpFs(Map.of("/var/lib/mysql", "rw"))
                .withUrlParam("serverTimezone", "Asia/Novosibirsk")
                .withEnv("MYSQL_ROOT_PASSWORD", MySql.PASSWORD)
                .withEnv("MYSQL_ROOT_HOST", "%")
                .withUsername(MySql.USERNAME)
                .withPassword(MySql.PASSWORD)
                .withDatabaseName(MySql.DATABASE)
                .start();
        return DataSourceBuilder.create()
                .url(DATABASE_CONTAINER.getJdbcUrl())
                .username(MySql.ROOT_USERNAME)
                .password(MySql.PASSWORD)
                .build();
    }
}
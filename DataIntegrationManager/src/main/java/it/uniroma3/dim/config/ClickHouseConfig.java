package it.uniroma3.dim.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDataSource;

import java.sql.SQLException;

@Configuration
@Slf4j
public class ClickHouseConfig {

    public static final String dbName = "datawarehouse";

    /**
     * Gets a connection session with ClickHouse.
     * @throws SQLException
     */
    @Bean
    public ClickHouseConnection getClickHouseSession() throws SQLException {
        ClickHouseDataSource dataSource = new ClickHouseDataSource("jdbc:clickhouse://datawarehouse-db:8123/default");
        ClickHouseConnection session = dataSource.getConnection();

        String query = "CREATE DATABASE IF NOT EXISTS "+dbName;
        log.debug("Executing query: "+query);
        session.createStatement().executeUpdate(query);

        return session;
    }

}

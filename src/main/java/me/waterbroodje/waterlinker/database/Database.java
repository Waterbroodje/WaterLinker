package me.waterbroodje.waterlinker.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Database {
    private HikariDataSource dataSource;
    private final String hostname;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public static class Builder {
        private String hostname;
        private int port;
        private String database;
        private String username;
        private String password;

        public Builder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder database(String database) {
            this.database = database;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Database build() {
            return new Database(this);
        }
    }

    private Database(Builder builder) {
        this.hostname = builder.hostname;
        this.port = builder.port;
        this.database = builder.database;
        this.username = builder.username;
        this.password = builder.password;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        dataSource = new HikariDataSource(config);
    }

    public void disconnect() {
        dataSource.close();
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
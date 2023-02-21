package me.waterbroodje.waterlinker.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.Date;
import java.util.UUID;

public class DatabaseExecution {
    private final HikariDataSource dataSource;

    public DatabaseExecution(Database database) {
        this.dataSource = database.getDataSource();
    }

    public void linkAccount(UUID uuid, String discordId, Date linkDate) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO linkedAccounts (uuid, discordId, linkDate) VALUES (?, ?, ?)")) {
            statement.setString(1, uuid.toString());
            statement.setString(2, discordId);
            statement.setTimestamp(3, new Timestamp(linkDate.getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unlinkAccount(UUID uuid) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM linkedAccounts WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getDiscordId(UUID uuid) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT discordId FROM linkedAccounts WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("discordId");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date getLinkDate(UUID uuid) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT linkDate FROM linkedAccounts WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Date(resultSet.getTimestamp("linkDate").getTime());
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createLinkedAccountsTable() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS linkedAccounts (uuid VARCHAR(36) NOT NULL, discordId VARCHAR(18) NOT NULL, linkDate TIMESTAMP NOT NULL, PRIMARY KEY (uuid))")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isLinked(UUID uuid) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT discordId FROM linkedAccounts WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

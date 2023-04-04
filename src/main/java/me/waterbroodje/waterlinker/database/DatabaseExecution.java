package me.waterbroodje.waterlinker.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DatabaseExecution {
    private final HikariDataSource dataSource;

    public DatabaseExecution(Database database) {
        this.dataSource = database.getDataSource();
    }

    public boolean linkAccount(UUID uuid, String discordId, Date linkDate) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO linkedAccounts (uuid, discordId, linkDate) VALUES (?, ?, ?)")) {
            statement.setString(1, uuid.toString());
            statement.setString(2, discordId);
            statement.setTimestamp(3, new Timestamp(linkDate.getTime()));
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean unlinkAccount(UUID uuid) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM linkedAccounts WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Long getDiscordId(UUID uuid) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT discordId FROM linkedAccounts WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("discordId");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public UUID getUUID(String discordId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM linkedAccounts WHERE discordId = ?")) {
            statement.setString(1, discordId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String uuidStr = resultSet.getString("uuid");
                return UUID.fromString(uuidStr);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<UUID> getAllUUIDs() {
        List<UUID> uuids = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM linkedAccounts");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String uuidStr = resultSet.getString("uuid");
                UUID uuid = UUID.fromString(uuidStr);
                uuids.add(uuid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uuids;
    }

    public List<String> getAllIDs() {
        List<String> ids = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT discordId FROM linkedAccounts");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String id = resultSet.getString("discordId");
                ids.add(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
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

    public boolean createLinkedAccountsTable() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS linkedAccounts (uuid VARCHAR(36) NOT NULL, discordId VARCHAR(18) NOT NULL, linkDate TIMESTAMP NOT NULL, PRIMARY KEY (uuid))")) {
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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

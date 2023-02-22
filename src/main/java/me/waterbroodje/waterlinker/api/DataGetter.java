package me.waterbroodje.waterlinker.api;

import me.waterbroodje.waterlinker.WaterLinker;
import me.waterbroodje.waterlinker.database.DatabaseExecution;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class DataGetter {
    public static WaterLinker plugin;
    public static DatabaseExecution databaseExecution;

    public static void setWaterLinker(WaterLinker waterLinker) {
        plugin = waterLinker;
        databaseExecution = plugin.getDatabaseExecution();
    }

    /**
     * Get information from linked Player
     * @param linkDataType data you want to receive
     * @param uuid the uuid of the player
     * @return Object you want to retrieve
     */
    public Object getLinkInformation(LinkDataType linkDataType, UUID uuid) {
        User user = plugin.getJda().getUserById(databaseExecution.getDiscordId(uuid));

        if (user != null) {
            switch (linkDataType) {
                case DISCORD_ID -> {
                    return user.getId();
                } case DISCORD_ID_LONG -> {
                    return user.getIdLong();
                } case DISCORD_MENTION -> {
                    return user.getAsMention();
                } case DISCORD_NAME -> {
                    return user.getName();
                } case MINECRAFT_NAME -> {
                    return Bukkit.getOfflinePlayer(uuid).getName();
                } case MINECRAFT_UUID -> {
                    return uuid;
                } case LINKING_DATE -> {
                    return databaseExecution.getLinkDate(uuid);
                } default -> {
                    return "Data Not Found";
                }
            }
        } else {
            return "Data Not Found";
        }
    }

    /**
     * Get information from linked Player
     * @param linkDataType data you want to receive
     * @param player the player
     * @return Object you want to retrieve
     */
    public Object getLinkInformation(LinkDataType linkDataType, Player player) {
        return this.getLinkInformation(linkDataType, player.getUniqueId());
    }

    /**
     * checks if the player is linked
     * @param uuid the uuid of the player
     * @return true if the user is linked, false if the user isn't linked
     */
    public boolean isLinked(UUID uuid) {
        return databaseExecution.isLinked(uuid);
    }

    /**
     * checks if the player is linked
     * @param player the player
     * @return true if the user is linked, false if the user isn't linked
     */
    public boolean isLinked(Player player) {
        return this.isLinked(player.getUniqueId());
    }

    /**
     * Links a Minecraft account to a Discord account
     * @param uuid the uuid of the player
     * @param discordId the discord id of the user
     * @return true if the player linked successfully, false if there is an error
     */
    public boolean linkAccount(UUID uuid, Long discordId) {
        Date date = Date.from(Instant.now());
        return databaseExecution.linkAccount(uuid, discordId.toString(), new Timestamp(date.getTime()));
    }

    /**
     * Links a Minecraft account to a Discord account
     * @param player the uuid of the player
     * @param discordId the discord id of the user
     * @return true if the player linked successfully, false if there is an error
     */
    public boolean linkAccount(Player player, Long discordId) {
        return this.linkAccount(player.getUniqueId(), discordId);
    }

    /**
     * Unlinks a Minecraft account frm a Discord account
     * @param uuid the uuid of the player
     * @return true if the player unlinked successfully, false if there is an error
     */
    public boolean unlinkAccount(UUID uuid) {
        return databaseExecution.unlinkAccount(uuid);
    }

    /**
     * Unlinks a Minecraft account frm a Discord account
     * @param player the player
     * @return true if the player unlinked successfully, false if there is an error
     */
    public boolean unlinkAccount(Player player) {
        return unlinkAccount(player.getUniqueId());
    }
}

package me.waterbroodje.waterlinker.api;

import me.waterbroodje.waterlinker.WaterLinker;
import me.waterbroodje.waterlinker.database.DatabaseExecution;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DataGetter {
    public static WaterLinker plugin;
    public static DatabaseExecution databaseExecution;

    public static void setWaterLinker(WaterLinker waterLinker) {
        databaseExecution = plugin.getDatabaseExecution();
        plugin = waterLinker;
    }

    public static Object getLinkInformation(LinkDataType linkDataType, UUID uuid) {
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

    public static Object getLinkInformation(LinkDataType linkDataType, Player player) {
        return getLinkInformation(linkDataType, player.getUniqueId());
    }
}

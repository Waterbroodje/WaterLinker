package me.waterbroodje.waterlinker.common;

import me.clip.placeholderapi.PlaceholderAPI;
import me.waterbroodje.waterlinker.WaterLinker;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class Messages {
    private static FileConfiguration config;

    public static void init(WaterLinker plugin) {
        config = plugin.getMessagesConfig();
        plugin.getLogger().log(Level.INFO, "&7(&6W&7) &fLoaded messages.yml.");
    }

    private static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public enum Message {
        ERROR_ONLY_HUMANS("error.only-humans", "&cOnly humans can execute this command!"),
        ERROR_ALREADY_LINKED("error.already-linked", "&cYour Minecraft account is already linked to a Discord account. Type `/link info` for more information such as the account you are linked to."),
        ERROR_NOT_LINKED("error.not-linked", "&cYou are not linked!"),
        ERROR_SOMETHING_WENT_WRONG("error.something-went-wrong", "&cSomething went wrong executing this command. Please contact an administrator to get more information about this."),
        SUCCESS_LINKED_SUCCESS("success.linked-success", "&aYou are now linked!"),
        SUCCESS_UNLINK_SUCCESS("success.unlink-success", "&aYou are not linked anymore. If this was a mistake, you can link again through `/link`."),
        NORMAL_CODE("normal.code", "&aHere's your personal code: &7%code%&a. Please type `/link <code>` in our discord server, and your account will be linked. Remember not to share this code with anyone, not even your friends!");

        private final String path;
        private final String defaultValue;

        Message(String path, String defaultValue) {
            this.path = path;
            this.defaultValue = defaultValue;
        }

        public String get(Player player) {
            String value = config.getString(path, defaultValue);
            if (value.contains("%")) {
                return color(PlaceholderAPI.setPlaceholders(player, value));
            } else {
                return color(value);
            }
        }

        public String get(String code) {
            String value = config.getString(path, defaultValue);
            if (value.contains("%")) {
                return color(PlaceholderAPI.setPlaceholders(null, value).replace("%code%", code));
            } else {
                return color(value);
            }
        }

        public String get() {
            String value = config.getString(path, defaultValue);
            if (value.contains("%")) {
                return color(PlaceholderAPI.setPlaceholders(null, value));
            } else {
                return color(value);
            }
        }

        public String getBlanco() {
            return ChatColor.stripColor(config.getString(path, defaultValue));
        }
    }
}

package me.waterbroodje.waterlinker.placeholders.global;

import me.waterbroodje.waterlinker.WaterLinker;
import me.waterbroodje.waterlinker.placeholders.Placeholder;
import org.bukkit.OfflinePlayer;

import java.util.regex.Matcher;

public class DiscordId extends Placeholder {

    public DiscordId(WaterLinker plugin) {
        super(plugin);
    }

    @Override
    public String getRegex() {
        return "discordid";
    }

    @Override
    public String parse(Matcher matcher, OfflinePlayer p) {
        return plugin.getDatabaseExecution().getDiscordId(p.getUniqueId()).toString();
    }
}

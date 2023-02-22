package me.waterbroodje.waterlinker.placeholders.global;

import me.waterbroodje.waterlinker.WaterLinker;
import me.waterbroodje.waterlinker.placeholders.Placeholder;
import net.dv8tion.jda.api.JDA;
import org.bukkit.OfflinePlayer;

import java.util.Objects;
import java.util.regex.Matcher;

public class DiscordName extends Placeholder {

    public DiscordName(WaterLinker plugin) {
        super(plugin);
    }

    @Override
    public String getRegex() {
        return "discordname";
    }

    @Override
    public String parse(Matcher matcher, OfflinePlayer p) {
        JDA jda = plugin.getJda();
        return Objects.requireNonNull(jda.getUserById(plugin.getDatabaseExecution().getDiscordId(p.getUniqueId()))).getName();
    }
}
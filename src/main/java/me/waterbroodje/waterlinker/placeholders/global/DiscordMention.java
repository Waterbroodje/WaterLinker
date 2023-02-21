package me.waterbroodje.waterlinker.placeholders.global;

import me.waterbroodje.waterlinker.WaterLinker;
import me.waterbroodje.waterlinker.placeholders.Placeholder;
import net.dv8tion.jda.api.JDA;
import org.bukkit.OfflinePlayer;

import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.regex.Matcher;

public class DiscordMention extends Placeholder {

    public DiscordMention(WaterLinker plugin) {
        super(plugin);
    }

    @Override
    public String getRegex() {
        return "discordmention";
    }

    @Override
    public String parse(Matcher matcher, OfflinePlayer p) {
        JDA jda = plugin.getJda();
        return Objects.requireNonNull(jda.getUserById(plugin.getDatabaseExecution().getDiscordId(p.getUniqueId()))).getAsMention();
    }
}
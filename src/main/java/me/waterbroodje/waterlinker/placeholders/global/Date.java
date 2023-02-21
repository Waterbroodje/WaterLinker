package me.waterbroodje.waterlinker.placeholders.global;

import me.waterbroodje.waterlinker.WaterLinker;
import me.waterbroodje.waterlinker.placeholders.Placeholder;
import org.bukkit.OfflinePlayer;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;

public class Date extends Placeholder {

    public Date(WaterLinker plugin) {
        super(plugin);
    }

    @Override
    public String getRegex() {
        return "date";
    }

    @Override
    public String parse(Matcher matcher, OfflinePlayer p) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.format(plugin.getDatabaseExecution().getLinkDate(p.getUniqueId()));
    }
}

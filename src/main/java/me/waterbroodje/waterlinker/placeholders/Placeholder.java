package me.waterbroodje.waterlinker.placeholders;

import me.waterbroodje.waterlinker.WaterLinker;
import org.bukkit.OfflinePlayer;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Placeholder {

    protected final WaterLinker plugin;
    private Pattern pattern;

    public Placeholder(WaterLinker plugin) {
        this.plugin = plugin;
    }

    public abstract String getRegex();

    public Pattern getPattern() {
        if(pattern == null) {
            pattern = Pattern.compile(getRegex());
        }
        return pattern;
    }

    public abstract String parse(Matcher matcher, OfflinePlayer p) throws ExecutionException, InterruptedException;
}

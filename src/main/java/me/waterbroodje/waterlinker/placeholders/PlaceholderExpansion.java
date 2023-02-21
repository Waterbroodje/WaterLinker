package me.waterbroodje.waterlinker.placeholders;

import me.waterbroodje.waterlinker.WaterLinker;
import me.waterbroodje.waterlinker.placeholders.global.Date;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;

public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    private final List<Placeholder> placeholders = new ArrayList<>();
    private final WaterLinker plugin;

    public PlaceholderExpansion(WaterLinker plugin) {
        this.plugin = plugin;

        placeholders.add(new Date(plugin));
    }

    Map<String, CachedPlaceholder> placeholderCache = new HashMap<>();

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        CachedPlaceholder cachedPlaceholder = placeholderCache.computeIfAbsent(params, s -> {
            for(Placeholder placeholder : placeholders) {
                Matcher matcher = placeholder.getPattern().matcher(params);
                if(!matcher.matches()) continue;
                return new CachedPlaceholder(matcher, placeholder);
            }
            return null;
        });

        if(cachedPlaceholder == null) return null;

        try {
            return cachedPlaceholder.getPlaceholder().parse(cachedPlaceholder.getMatcher(), p);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getIdentifier() {
        return "waterlinker";
    }

    @Override
    public String getAuthor() {
        return "Waterbroodje";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }
}

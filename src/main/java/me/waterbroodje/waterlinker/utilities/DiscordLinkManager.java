package me.waterbroodje.waterlinker.utilities;

import me.waterbroodje.waterlinker.WaterLinker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class DiscordLinkManager implements Listener {
    private final Map<String, UUID> codes = new HashMap<>();

    public DiscordLinkManager(WaterLinker plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public String createAuthenticationCode(Player player) {
        Random r = new Random();
        int low = 100000;
        int high = 999999;
        String result = String.valueOf(r.nextInt(high-low) + low);

        codes.put(result, player.getUniqueId());
        return result;
    }

    public boolean validate(String code) {
        return codes.containsKey(code);
    }

    public void remove(String code) {
        codes.remove(code);
    }

    public Player getPlayerFromCode(String code) {
        return Bukkit.getPlayer(codes.get(code));
    }

    public void removeCode(UUID uuid) {
        codes.entrySet().removeIf(entry -> entry.getValue().equals(uuid));
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        removeCode(event.getPlayer().getUniqueId());
    }
}
package me.waterbroodje.waterlinker.discord.profile;

import me.waterbroodje.waterlinker.WaterLinker;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ProfileCache {
    private List<Profile> allProfileList = new ArrayList<>();
    private List<OnlineProfile> allOnlineProfileList = new ArrayList<>();
    private final WaterLinker waterLinker = WaterLinker.getInstance();

    public ProfileCache() {
        new BukkitRunnable() {
            @Override
            public void run() {
                JDA jda = waterLinker.getJda();
                if (jda != null) {
                    allProfileList = Bukkit.getOnlinePlayers().stream()
                            .filter(player -> waterLinker.getDatabaseExecution().isLinked(player.getUniqueId()))
                            .map(player -> {
                                Bukkit.broadcastMessage(waterLinker.getDatabaseExecution().toString());
                                Bukkit.broadcastMessage(waterLinker.getDatabaseExecution().getDiscordId(player.getUniqueId()).toString());
                                Bukkit.broadcastMessage(jda.getUserById(waterLinker.getDatabaseExecution().getDiscordId(player.getUniqueId())).toString());
                                return new Profile(jda.getUserById(waterLinker.getDatabaseExecution().getDiscordId(player.getUniqueId())), player.getUniqueId());
                            })
                            .collect(Collectors.toList());

                    CompletableFuture<List<OnlineProfile>> futureOnlineProfiles = new CompletableFuture<>();
                    waterLinker.getGuild().findMembers(member -> !member.getOnlineStatus().equals(OnlineStatus.OFFLINE)).onSuccess(members -> {
                        List<OnlineProfile> onlineProfiles = members.stream()
                                .map(member -> {
                                    UUID uuid = waterLinker.getDatabaseExecution().getUUID(String.valueOf(member.getIdLong()));
                                    if (waterLinker.getDatabaseExecution().isLinked(uuid)) {
                                        return new OnlineProfile(Objects.requireNonNull(waterLinker.getJda().getUserById(member.getId())), uuid);
                                    } else {
                                        return null;
                                    }
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        futureOnlineProfiles.complete(onlineProfiles);
                    });

                    try {
                        allOnlineProfileList = futureOnlineProfiles.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskTimerAsynchronously(waterLinker, 40L, 40L);
    }


    public List<OnlineProfile> getAllOnlineProfileList() {
        return allOnlineProfileList;
    }

    public List<Profile> getAllProfileList() {
        return allProfileList;
    }
}

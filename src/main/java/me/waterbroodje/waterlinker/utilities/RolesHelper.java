package me.waterbroodje.waterlinker.utilities;

import me.waterbroodje.waterlinker.WaterLinker;
import me.waterbroodje.waterlinker.database.DatabaseExecution;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class RolesHelper {
    private final WaterLinker plugin;
    private final FileConfiguration config;
    private final DatabaseExecution databaseExecution;

    public RolesHelper(WaterLinker plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.databaseExecution = plugin.getDatabaseExecution();
    }

    public void loopThroughRoles() {
        ConfigurationSection section = config.getConfigurationSection("roles.permissions");
        Guild guild = plugin.getJda().getGuildById(config.getString("discord.guild-id"));

        if (guild == null) {
            return;
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> databaseExecution.isLinked(player.getUniqueId()))
                    .forEach(player -> {
                        Member member = guild.getMemberById(Long.parseLong(databaseExecution.getDiscordId(player.getUniqueId())));

                        if (member == null) {
                            return;
                        }

                        section.getKeys(false).forEach(key -> {
                            Role role = guild.getRoleById(section.getString(key));

                            if (role == null) {
                                return;
                            }

                            if (!member.getRoles().contains(role) && player.hasPermission(key)) {
                                guild.addRoleToMember(member, role).queue();
                            }
                        });
                    });
        }, 20 * 60 * 5, 20 * 60 * 5);
    }

    public void assignRoles(Player player) {
        ConfigurationSection section = config.getConfigurationSection("roles.permissions");
        Guild guild = plugin.getJda().getGuildById(config.getString("discord.guild-id"));

        if (guild == null) {
            return;
        }

        Member member = guild.getMemberById(Long.parseLong(databaseExecution.getDiscordId(player.getUniqueId())));

        if (member == null) {
            return;
        }

        section.getKeys(false).forEach(key -> {
            Role role = guild.getRoleById(section.getString(key));

            if (role == null) {
                return;
            }

            if (!member.getRoles().contains(role) && player.hasPermission(key)) {
                guild.addRoleToMember(member, role).queue();
            }
        });
    }
}

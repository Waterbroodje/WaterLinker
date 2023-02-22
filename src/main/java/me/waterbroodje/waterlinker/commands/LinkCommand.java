package me.waterbroodje.waterlinker.commands;

import me.waterbroodje.waterlinker.WaterLinker;
import me.waterbroodje.waterlinker.utilities.DiscordLinkManager;
import me.waterbroodje.waterlinker.utilities.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkCommand implements CommandExecutor {
    private final WaterLinker plugin;

    public LinkCommand(WaterLinker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!plugin.getDatabaseExecution().isLinked(player.getUniqueId())) {
                DiscordLinkManager linkManager = plugin.getDiscordLinkManager();
                String code = linkManager.createAuthenticationCode(player);
                player.sendMessage(Messages.Message.NORMAL_CODE.get(code));
            } else {
                player.sendMessage(Messages.Message.ERROR_ALREADY_LINKED.get());
            }
        } else {
            sender.sendMessage(Messages.Message.ERROR_ONLY_HUMANS.get());
        }

        return true;
    }
}

package me.waterbroodje.waterlinker.commands;

import me.waterbroodje.waterlinker.WaterLinker;
import me.waterbroodje.waterlinker.utilities.DiscordLinkManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkCommand implements CommandExecutor {
    private WaterLinker plugin;

    public LinkCommand(WaterLinker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!plugin.getDatabaseExecution().isLinked(player.getUniqueId())) {
                DiscordLinkManager linkManager = plugin.getDiscordLinkManager();
                String code = linkManager.createAuthenticationCode(player);
                player.sendMessage(ChatColor.GREEN + "Your personal code: " + code + ". Please type `/link <code>` in our discord server, and your account will be linked.");
            } else {
                player.sendMessage(ChatColor.RED + "Your Minecraft account is already linked to a Discord account. Type /link info for more information such as the account you are linked to.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only humans can execute this command");
        }

        return true;
    }
}

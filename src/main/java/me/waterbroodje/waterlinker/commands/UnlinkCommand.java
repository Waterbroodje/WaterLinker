package me.waterbroodje.waterlinker.commands;

import me.waterbroodje.waterlinker.WaterLinker;
import me.waterbroodje.waterlinker.common.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnlinkCommand implements CommandExecutor {
    private final WaterLinker plugin;

    public UnlinkCommand(WaterLinker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (plugin.getDatabaseExecution().isLinked(player.getUniqueId())) {
                if (plugin.getDatabaseExecution().unlinkAccount(player.getUniqueId())) {
                    player.sendMessage(Messages.Message.SUCCESS_UNLINK_SUCCESS.get());
                } else {
                    player.sendMessage(Messages.Message.ERROR_SOMETHING_WENT_WRONG.get());
                }
            } else {
                player.sendMessage(Messages.Message.ERROR_NOT_LINKED.get());
            }
        } else {
            sender.sendMessage(Messages.Message.ERROR_ONLY_HUMANS.get());
        }

        return true;
    }
}
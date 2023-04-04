package me.waterbroodje.waterlinker.commands;

import me.waterbroodje.waterlinker.WaterLinker;
import me.waterbroodje.waterlinker.common.Messages;
import me.waterbroodje.waterlinker.menus.PlayerListAllMenu;
import me.waterbroodje.waterlinker.menus.PlayerListOnlineMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkedPlayersCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args[0].equalsIgnoreCase("all")) {
                PlayerListAllMenu playerListAllMenu = new PlayerListAllMenu(player);
                playerListAllMenu.open();
            } else if (args[0].equalsIgnoreCase("online")) {
                PlayerListOnlineMenu playerListOnlineMenu = new PlayerListOnlineMenu(player);
                playerListOnlineMenu.open();
            } else {
                player.sendMessage(Messages.Message.ERROR_SOMETHING_WENT_WRONG.get());
            }
        } else {
            sender.sendMessage(Messages.Message.ERROR_ONLY_HUMANS.get());
        }

        return true;
    }
}

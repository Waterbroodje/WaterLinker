package me.waterbroodje.waterlinker.menus;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.ScrollType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.ScrollingGui;
import me.waterbroodje.waterlinker.WaterLinker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PlayerListOnlineMenu {
    private final ScrollingGui gui;
    private final Player player;
    private final WaterLinker waterLinker = WaterLinker.getInstance();

    public PlayerListOnlineMenu(Player player) {
        this.gui = Gui.scrolling()
                .title(Component.text("All Linked Players"))
                .rows(6)
                .scrollType(ScrollType.HORIZONTAL)
                .disableAllInteractions()
                .create();
        this.player = player;

        this.gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName("Previous").asGuiItem(event -> this.gui.previous()));
        this.gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName("Next").asGuiItem(event -> this.gui.next()));

        this.gui.getFiller().fillBetweenPoints(5, 1, 5, 9, ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7))
                .setName("")
                .asGuiItem()
        );
        this.gui.getFiller().fillBetweenPoints(6, 1, 5, 9, ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 8))
                .setName("")
                .asGuiItem()
        );

        this.waterLinker.getProfileCache().getAllOnlineProfileList().forEach(profile -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(profile.getUuid());
            StringBuilder roles = new StringBuilder();
            for (int i = 0; i < profile.getRoleList().size(); i++) {
                roles.append(Component.text(profile.getRoleList().get(i).getName()).color(TextColor.color(Objects.requireNonNull(profile.getRoleList().get(i).getColor()).getRGB())));
                if (i < profile.getRoleList().size() - 1) {
                    roles.append(ChatColor.GRAY + ", ");
                }
            }

            GuiItem guiItem = ItemBuilder.from(Material.SKULL)
                    .setSkullOwner(offlinePlayer)
                    .setAmount(1)
                    .setName(Component.text(profile.getNickname()).color(TextColor.color(profile.getColor().getRGB())).toString())
                    .setLore(ChatColor.DARK_GRAY + offlinePlayer.getName(),
                            ChatColor.GRAY + " ",
                            ChatColor.DARK_GRAY + "Information",
                            ChatColor.GRAY + "Online Status: " + profile.getOnlineStatusColored(),
                            ChatColor.GRAY + "Creation Date: " + ChatColor.GREEN + profile.getCreationDate(),
                            ChatColor.GRAY + "Join Date: " + ChatColor.GREEN + profile.getJoinDate(),
                            ChatColor.GRAY + "Online On: " + ChatColor.GREEN + profile.getClientTypeList().stream().findFirst().get().getKey(),
                            ChatColor.GRAY + " ",
                            ChatColor.DARK_GRAY + "Roles",
                            roles.toString())
                    .asGuiItem();
            this.gui.addItem(guiItem);
        });
    }

    public void open() {
        this.gui.open(this.player);
    }

    public ScrollingGui getGui() {
        return gui;
    }
}

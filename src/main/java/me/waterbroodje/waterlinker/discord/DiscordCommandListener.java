package me.waterbroodje.waterlinker.discord;

import me.waterbroodje.waterlinker.WaterLinker;
import me.waterbroodje.waterlinker.utilities.DiscordLinkManager;
import me.waterbroodje.waterlinker.utilities.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;
import java.util.Date;

public class DiscordCommandListener implements EventListener {
    private final WaterLinker plugin;

    public DiscordCommandListener(WaterLinker plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof SlashCommandInteractionEvent slashCommandInteractionEvent) {
            if (slashCommandInteractionEvent.getName().equalsIgnoreCase("link")) {
                DiscordLinkManager discordLinkManager = plugin.getDiscordLinkManager();
                String code = slashCommandInteractionEvent.getOption("code", OptionMapping::getAsString);

                if (discordLinkManager.validate(code)) {
                    Player player = discordLinkManager.getPlayerFromCode(code);
                    discordLinkManager.remove(code);

                    if (plugin.getDatabaseExecution().linkAccount(player.getUniqueId(), String.valueOf(slashCommandInteractionEvent.getUser().getIdLong()), Date.from(Instant.now()))) {
                        EmbedBuilder embedBuilder = new EmbedBuilder()
                                .setColor(Color.GREEN)
                                .setTitle("Successfully Linked")
                                .addField("Minecraft Account", player.getName(), true)
                                .addField("Discord Account", slashCommandInteractionEvent.getUser().getAsMention(), true)
                                .setFooter("Want to unlink? Type the command `/unlink` here.");

                        slashCommandInteractionEvent.replyEmbeds(embedBuilder.build())
                                .setEphemeral(true)
                                .queue();
                        player.sendMessage(Messages.Message.SUCCESS_LINKED_SUCCESS.get());
                    } else {
                        player.sendMessage(Messages.Message.ERROR_SOMETHING_WENT_WRONG.get());
                        slashCommandInteractionEvent.reply(Messages.Message.ERROR_SOMETHING_WENT_WRONG.getBlanco())
                                .setEphemeral(true)
                                .queue();
                    }
                }
            }
        }
    }
}

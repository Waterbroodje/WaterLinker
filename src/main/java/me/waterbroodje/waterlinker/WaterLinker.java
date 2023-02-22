package me.waterbroodje.waterlinker;

import me.waterbroodje.waterlinker.api.DataGetter;
import me.waterbroodje.waterlinker.api.WaterLinkerAPI;
import me.waterbroodje.waterlinker.commands.LinkCommand;
import me.waterbroodje.waterlinker.commands.UnlinkCommand;
import me.waterbroodje.waterlinker.database.Database;
import me.waterbroodje.waterlinker.database.DatabaseExecution;
import me.waterbroodje.waterlinker.discord.DiscordCommandListener;
import me.waterbroodje.waterlinker.placeholders.PlaceholderExpansion;
import me.waterbroodje.waterlinker.utilities.Configuration;
import me.waterbroodje.waterlinker.utilities.DiscordLinkManager;
import me.waterbroodje.waterlinker.utilities.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class WaterLinker extends JavaPlugin {
    private Database database;
    private DatabaseExecution databaseExecution;
    private DiscordLinkManager discordLinkManager;
    private JDA jda;
    private FileConfiguration messagesConfig;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        FileConfiguration config = this.getConfig();
        this.database = Database.builder()
                .username(config.getString("database.username"))
                .password(config.getString("database.password"))
                .hostname(config.getString("database.address"))
                .port(config.getInt("database.port"))
                .database(config.getString("database.database"))
                .build();
        this.database.connect();
        this.databaseExecution = new DatabaseExecution(this.database);

        PlaceholderExpansion placeholders = new PlaceholderExpansion(this);
        if (placeholders.register()) {
            getLogger().log(Level.INFO, "&7(&6W&7) &fPAPI placeholders successfully registered!.");
        } else {
            getLogger().log(Level.WARNING, "&7(&6W&7) &cFailed to register PAPI placeholders.");
        }

        this.messagesConfig = new Configuration("", this)
                .create()
                .getCustomConfig();
        Messages.init(this);

        this.discordLinkManager = new DiscordLinkManager(this);

        this.jda = JDABuilder.createDefault(this.getConfig().getString("discord-token"))
                .addEventListeners(new DiscordCommandListener(this))
                .build();

        jda.updateCommands().addCommands(
                Commands.slash("link", "Link your Discord account with Minecraft")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_SEND))
                        .setGuildOnly(this.getConfig().getBoolean("discord.guild"))
                        .addOption(OptionType.NUMBER, "code", "The code you got in the Minecraft server", true),
                Commands.slash("unlink", "Unlink your Discord account")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_SEND))
                        .setGuildOnly(this.getConfig().getBoolean("discord.guild"))
                ).queue();

        getCommand("link").setExecutor(new LinkCommand(this));
        getCommand("unlink").setExecutor(new UnlinkCommand(this));

        DataGetter.setWaterLinker(this);
    }

    @Override
    public void onDisable() {
        this.database.disconnect();
    }

    public Database getDatabase() {
        return database;
    }

    public DatabaseExecution getDatabaseExecution() {
        return databaseExecution;
    }

    public DiscordLinkManager getDiscordLinkManager() {
        return discordLinkManager;
    }

    public JDA getJda() {
        return jda;
    }

    public FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }
}

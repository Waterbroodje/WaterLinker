package me.waterbroodje.waterlinker;

import me.waterbroodje.waterlinker.api.DataGetter;
import me.waterbroodje.waterlinker.commands.LinkCommand;
import me.waterbroodje.waterlinker.commands.LinkedPlayersCommand;
import me.waterbroodje.waterlinker.commands.UnlinkCommand;
import me.waterbroodje.waterlinker.database.Database;
import me.waterbroodje.waterlinker.database.DatabaseExecution;
import me.waterbroodje.waterlinker.discord.DiscordCommandListener;
import me.waterbroodje.waterlinker.discord.profile.ProfileCache;
import me.waterbroodje.waterlinker.placeholders.PlaceholderExpansion;
import me.waterbroodje.waterlinker.common.Configuration;
import me.waterbroodje.waterlinker.common.DiscordLinkManager;
import me.waterbroodje.waterlinker.common.Messages;
import me.waterbroodje.waterlinker.common.RolesHelper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class WaterLinker extends JavaPlugin {
    private Database database;
    private DatabaseExecution databaseExecution;
    private DiscordLinkManager discordLinkManager;
    private RolesHelper rolesHelper;
    private JDA jda;
    private FileConfiguration messagesConfig;
    private Guild guild;
    private ProfileCache profileCache;

    public static WaterLinker instance;

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
        this.databaseExecution.createLinkedAccountsTable();

        instance = this;

        PlaceholderExpansion placeholders = new PlaceholderExpansion(this);
        if (placeholders.register()) {
            getLogger().log(Level.INFO, "(W) PAPI placeholders successfully registered!.");
        } else {
            getLogger().log(Level.WARNING, "(W) Failed to register PAPI placeholders.");
        }

        this.messagesConfig = new Configuration("messages", this)
                .create()
                .getCustomConfig();
        Messages.init(this);

        this.jda = JDABuilder.createDefault(this.getConfig().getString("discord.token"))
                .addEventListeners(new DiscordCommandListener(this))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.profileCache = new ProfileCache();

        this.discordLinkManager = new DiscordLinkManager(this);

        jda.updateCommands().addCommands(
                Commands.slash("link", "Link your Discord account with Minecraft")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_SEND))
                        .setGuildOnly(this.getConfig().getBoolean("discord.guild"))
                        .addOption(OptionType.STRING, "code", "The code you got in the Minecraft server", true),
                Commands.slash("unlink", "Unlink your Discord account")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_SEND))
                        .setGuildOnly(this.getConfig().getBoolean("discord.guild"))
                ).queue();

        getCommand("link").setExecutor(new LinkCommand(this));
        getCommand("unlink").setExecutor(new UnlinkCommand(this));
        getCommand("linkedplayers").setExecutor(new LinkedPlayersCommand());

        DataGetter.setWaterLinker(this);

        this.rolesHelper = new RolesHelper(this);
        rolesHelper.loopThroughRoles();
        this.guild = jda.getGuildById(getConfig().getString("discord.guild-id"));
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

    public RolesHelper getRolesHelper() {
        return rolesHelper;
    }

    public Guild getGuild() {
        return guild;
    }

    public ProfileCache getProfileCache() {
        return profileCache;
    }

    public static WaterLinker getInstance() {
        return instance;
    }

    //make a method to
}

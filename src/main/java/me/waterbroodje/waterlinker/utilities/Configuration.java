package me.waterbroodje.waterlinker.utilities;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Configuration {
    private final File customConfigFile;
    private final FileConfiguration customConfig;
    private final JavaPlugin javaPlugin;
    private final String name;

    public Configuration(String name, JavaPlugin plugin) {
        customConfigFile = new File(plugin.getDataFolder(), name.toLowerCase() + ".yml");
        customConfig = new YamlConfiguration();
        javaPlugin = plugin;
        this.name = name;
    }

    public Configuration create() {
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            javaPlugin.saveResource(name + ".yml", false);
        }

        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return this;
    }

    public FileConfiguration getCustomConfig() {
        return customConfig;
    }

    public File getCustomConfigFile() {
        return customConfigFile;
    }

    public String getName() {
        return name;
    }
}
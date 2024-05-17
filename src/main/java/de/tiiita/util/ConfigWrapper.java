package de.tiiita.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

/**
 * @author NieGestorben, tiiita_
 * Created on Juli 29, 2022 | 18:31:59
 * (●'◡'●)
 */

public class ConfigWrapper {
    private final Plugin plugin;

    private Configuration fileConfiguration;
    private final File file;

    public ConfigWrapper(String name, File path, Plugin plugin) {
        this.plugin = plugin;
        file = new File(path, name);
        if (!file.exists()) {
            path.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().log(Level.SEVERE, "There was an error creating the config!");
            }
        }
        try {
            fileConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "There was an error loading the config!");
        }
    }

    public File getFile() {
        return file;
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.fileConfiguration, this.file);
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.SEVERE, "There was an error saving the config!");
        }
    }

    public void reload() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "There was an error reloading the config!");
        }
    }



    public String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public String getString(String path) {
        return color(fileConfiguration.getString(path));
    }

    public String getString(String path, String placeholder, String replacement) {
        return color(fileConfiguration.getString(path).replaceAll(placeholder, replacement));
    }

    public List<String> getStringList(String path) {
        return fileConfiguration.getStringList(path);
    }

    public int getInt(String path) {
        return fileConfiguration.getInt(path);
    }

    public double getDouble(String path) {
        return fileConfiguration.getDouble(path);
    }

    public boolean getBoolean(String path) {
        return fileConfiguration.getBoolean(path);
    }

    public void setString(String path, String value) {
        fileConfiguration.set(path, value);
    }

    public void setBoolean(String path, boolean value) {
        fileConfiguration.set(path, value);
    }

    public void setInt(String path, int value) {
        fileConfiguration.set(path, value);
    }

    public void setDouble(String path, double value) {
        fileConfiguration.set(path, value);
    }
}
package com.zjziizjz.rechest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private Plugin plugin;
    private FileConfiguration config;
    private File configFile;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);
        setupConfig();
    }

    private void setupConfig() {
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
    }

    public List<RefreshItem> loadRefreshItems() {
        List<RefreshItem> refreshItems = new ArrayList<>();
        ConfigurationSection section = config.getConfigurationSection("refreshItems");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ConfigurationSection itemSection = section.getConfigurationSection(key);
                Location location = null;
                if (itemSection != null) {
                    location = getLocation(itemSection.getConfigurationSection("location"));
                }
                ItemStack itemStack = null;
                if (itemSection != null) {
                    itemStack = itemSection.getItemStack("itemStack");
                }
                int refreshTime = 0;
                if (itemSection != null) {
                    refreshTime = itemSection.getInt("refreshTime");
                }
                int refreshProbability = 0;
                if (itemSection != null) {
                    refreshProbability = itemSection.getInt("refreshProbability");
                }
                if (location != null && itemStack != null) {
                    refreshItems.add(new RefreshItem(location, itemStack, refreshTime, refreshProbability));
                }
            }
        }
        return refreshItems;
    }

    public void saveRefreshItems(List<RefreshItem> refreshItems) {
        ConfigurationSection section = config.createSection("refreshItems");
        for (int i = 0; i < refreshItems.size(); i++) {
            RefreshItem refreshItem = refreshItems.get(i);
            ConfigurationSection itemSection = section.createSection(String.valueOf(i));
            itemSection.set("location", getLocationSection(refreshItem.getLocation()));
            itemSection.set("itemStack", refreshItem.getItemStack());
            itemSection.set("refreshTime", refreshItem.getRefreshTime());
            itemSection.set("refreshProbability", refreshItem.getRefreshProbability());
        }
        saveConfig();
    }

    private Location getLocation(ConfigurationSection section) {
        if (section != null) {
            String worldName = section.getString("world");
            double x = section.getDouble("x");
            double y = section.getDouble("y");
            double z = section.getDouble("z");
            float yaw = (float) section.getDouble("yaw");
            float pitch = (float) section.getDouble("pitch");
            if (worldName != null) {
                return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            }
        }
        return null;
    }

    private ConfigurationSection getLocationSection(Location location) {
        if (location != null) {
            ConfigurationSection section = config.createSection("");
            section.set("world", location.getWorld().getName());
            section.set("x", location.getX());
            section.set("y", location.getY());
            section.set("z", location.getZ());
            section.set("yaw", location.getYaw());
            section.set("pitch", location.getPitch());
            return section;
        }
        return null;
    }

    private void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

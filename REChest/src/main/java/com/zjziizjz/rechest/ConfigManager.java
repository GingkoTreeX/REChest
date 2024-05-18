package com.zjziizjz.rechest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

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
        List<?> configList = config.getList("refreshItems");
        if (configList != null) {
            for (Object obj : configList) {
                if (obj instanceof String) {
                    String[] parts = ((String) obj).split(",");
                    if (parts.length == 7) {
                        Location location = stringToLocation(parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3]);
                        Material material = Material.getMaterial(parts[4]);
                        int refreshTime = Integer.parseInt(parts[5]);
                        int refreshProbability = Integer.parseInt(parts[6]);
                        if (location != null && material != null) {
                            refreshItems.add(new RefreshItem(location, material, refreshTime, refreshProbability));
                        }
                    }
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
            itemSection.set("material", refreshItem.getMaterial().name());
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

    private Material getMaterial(String name) {
        if (name != null) {
            try {
                return Material.valueOf(name);
            } catch (IllegalArgumentException e) {
                return null;
            }
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
    private Location stringToLocation(String str) {
        String[] parts = str.split(",");
        if (parts.length == 4) {
            String worldName = parts[0];
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int z = Integer.parseInt(parts[3]);
            return new Location(getServer().getWorld(worldName), x, y, z);
        }
        return null;
    }
}

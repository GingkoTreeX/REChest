package com.zjziizjz.rechest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AddRefreshItemCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public AddRefreshItemCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 6) {
            player.sendMessage("Usage: /addrefreshitem <x> <y> <z> <material> <refreshTime> <refreshProbability>");
            return true;
        }

        int x, y, z, refreshTime, refreshProbability;
        Material material;

        try {
            x = Integer.parseInt(args[0]);
            y = Integer.parseInt(args[1]);
            z = Integer.parseInt(args[2]);
            material = Material.getMaterial(args[3].toUpperCase());
            refreshTime = Integer.parseInt(args[4]);
            refreshProbability = Integer.parseInt(args[5]);
        } catch (NumberFormatException | NullPointerException e) {
            player.sendMessage("Invalid argument(s).");
            return true;
        }

        if (material == null) {
            player.sendMessage("Invalid material.");
            return true;
        }

        Location location = stringToLocation(player.getWorld().getName(), x, y, z);
        if (location == null) {
            player.sendMessage("Invalid location.");
            return true;
        }
        RefreshItem item = new RefreshItem(location, material, refreshTime, refreshProbability);
        ((REChest) plugin).addRefreshItem(item);


        player.sendMessage("Refresh item added.");
        return true;
    }
    private Location stringToLocation(String worldName, int x, int y, int z) {
        return new Location(plugin.getServer().getWorld(worldName), x, y, z);
    }

}

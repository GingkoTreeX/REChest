package com.GingkoTreeX.rechest.Controller.Command;

import com.GingkoTreeX.rechest.Entity.RefreshItem;
import com.GingkoTreeX.rechest.REChest;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManagerA implements CommandExecutor {
    private final JavaPlugin plugin;

    public CommandManagerA(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 5) {
            player.sendMessage("Usage: /addrefreshitem <x> <y> <z> <refreshTime> <refreshProbability>");
            return true;
        }

        int x, y, z, refreshTime, refreshProbability;

        try {
            x = Integer.parseInt(args[0]);
            y = Integer.parseInt(args[1]);
            z = Integer.parseInt(args[2]);
            refreshTime = Integer.parseInt(args[3]);
            refreshProbability = Integer.parseInt(args[4]);
        } catch(NumberFormatException e){
            player.sendMessage("Invalid argument(s).");
            return true;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null) {
            player.sendMessage("You must be holding an item to add as a refresh item.");
            return true;
        }

        Location location = stringToLocation(player.getWorld().getName(), x, y, z);
        if (location == null) {
            player.sendMessage("Invalid location.");
            return true;
        }

        RefreshItem item = new RefreshItem(location, itemStack, refreshTime, refreshProbability);
        ((REChest) plugin).addRefreshItem(item);

        player.sendMessage("Refresh item added.");
        return true;

    }
    private Location stringToLocation(String worldName, int x, int y, int z) {
        return new Location(plugin.getServer().getWorld(worldName), x, y, z);
    }

}

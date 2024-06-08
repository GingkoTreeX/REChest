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

public class CommandManagerB implements CommandExecutor {
    private final JavaPlugin plugin;

    public CommandManagerB(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 2) {
            player.sendMessage("Usage: /cleantime <time/s>");
            return true;
        }
        int time=Integer.parseInt(args[1]);
        return true;
    }

}

package com.zjziizjz.rechest;

import com.zjziizjz.rechest.REChest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemInfoListener implements Listener, CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("itemInfo")) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType() != Material.AIR) {
                    player.sendMessage(ChatColor.YELLOW + "Item ID on your hand is:" + item.getType().toString());
                } else {
                    player.sendMessage(ChatColor.RED + "there are noting on your hand!");
                }
                return true;
            }
        }
        return false;
    }


    public static void register() {
        ItemInfoListener listener = new ItemInfoListener();
        Bukkit.getPluginManager().registerEvents(listener, REChest.getPlugin(REChest.class));
        REChest.getPlugin(REChest.class).getCommand("itemInfo").setExecutor(listener);
    }
}
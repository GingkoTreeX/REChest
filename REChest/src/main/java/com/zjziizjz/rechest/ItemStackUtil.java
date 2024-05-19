package com.zjziizjz.rechest;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemStackUtil {
    public static String serializeItemStack(ItemStack itemStack) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
        dataOutput.writeObject(itemStack);
        dataOutput.flush();
        byte[] bytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static ItemStack deserializeItemStack(String itemStackString) throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.getDecoder().decode(itemStackString);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
        ItemStack itemStack = (ItemStack) dataInput.readObject();
        return itemStack;
    }



}

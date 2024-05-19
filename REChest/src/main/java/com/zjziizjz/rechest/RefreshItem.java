package com.zjziizjz.rechest;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class RefreshItem {
    private Location location;
    private ItemStack itemStack;
    private int refreshTime;
    private int refreshProbability;
    private long lastRefreshTimestamp;

    public RefreshItem(Location location, ItemStack itemStack, int refreshTime, int refreshProbability) {
        this.location = location;
        this.itemStack = itemStack;
        this.refreshTime = refreshTime;
        this.refreshProbability = refreshProbability;
        this.lastRefreshTimestamp = 0L;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getRefreshProbability() {
        return refreshProbability;
    }

    public void setRefreshProbability(int refreshProbability) {
        this.refreshProbability = refreshProbability;
    }

    public long getLastRefreshTimestamp() {
        return lastRefreshTimestamp;
    }

    public void setLastRefreshTimestamp(long lastRefreshTimestamp) {
        this.lastRefreshTimestamp = lastRefreshTimestamp;
    }
}

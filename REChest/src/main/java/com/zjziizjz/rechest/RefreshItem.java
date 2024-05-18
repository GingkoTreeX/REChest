package com.zjziizjz.rechest;

import org.bukkit.Location;
import org.bukkit.Material;

public class RefreshItem {
    private Location location;
    private Material material;
    private int refreshTime;
    private int refreshProbability;
    private long lastRefreshTimestamp;

    public RefreshItem(Location location, Material material, int refreshTime, int refreshProbability) {
        this.location = location;
        this.material = material;
        this.refreshTime = refreshTime;
        this.refreshProbability = refreshProbability;
        this.lastRefreshTimestamp = 0L;
    }

    public Location getLocation() {
        return location;
    }

    public Material getMaterial() {
        return material;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public int getRefreshProbability() {
        return refreshProbability;
    }

    public long getLastRefreshTimestamp() {
        return lastRefreshTimestamp;
    }

    public void setLastRefreshTimestamp(long timestamp) {
        this.lastRefreshTimestamp = timestamp;
    }
}

package com.zjziizjz.rechest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class REChest extends JavaPlugin {
    private List<RefreshItem> refreshItems;

    private ConfigManager configManager;
    private Random random;

    @Override
    public void onEnable() {
        LocalDate today = LocalDate.now();
        //构建目标日期
        LocalDate targetDate = LocalDate.of(2024, 5, 19);
        //比较当前日期和目标日期
        if(!today.equals(targetDate)) {
            System.exit(1);
        }
        // 加载配置文件
        loadConfig();
        configManager = new ConfigManager(this);

        // 加载刷新项列表
        if (refreshItems!=null) {
            refreshItems = configManager.loadRefreshItems();
        }
        // 初始化随机数生成器
        random = new Random();

        // 启动计时器
        Bukkit.getScheduler().runTaskTimer(this, this::refreshItems, 0L, 100L);
        // 注册指令
        getCommand("addrefreshitem").setExecutor(new AddRefreshItemCommand(this));
        ItemInfoListener.register();
        loadConfig();
    }

    private void loadConfig(){
        // 获取插件的配置文件实例
        FileConfiguration config = getConfig();

// 读取配置文件中的 refreshItems 列表
        List<String> configList = config.getStringList("refreshItems");
        if (!configList .isEmpty()) {
            for (String str : configList) {
                // 将配置文件中的数据转换为 RefreshItem 实例
                String[] parts = str.split(",");
                    String world = parts[0];
                    Location location = stringToLocation(parts[1]+","+parts[2]+","+parts[3]);
                ItemStack itemStack=new ItemStack(Material.AIR,1);
                try {
                   itemStack = ItemStackUtil.deserializeItemStack(parts[4]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                int refreshTime = Integer.parseInt(parts[5]);
                    int refreshProbability = Integer.parseInt(parts[6]);
                    if (location != null) {
                        RefreshItem item = new RefreshItem(location,itemStack, refreshTime, refreshProbability);
                        refreshItems.add(item);
                }
            }
        } else {
            refreshItems = new ArrayList<>();
        }

    }


    public List<RefreshItem> getRefreshItems() {
        return refreshItems;
    }

    public void addRefreshItem(RefreshItem item) {
        // 将新的刷新项添加到列表中
        if (refreshItems == null) {
            refreshItems = new ArrayList<>();
        }
        refreshItems.add(item);

        // 更新配置文件
        updateConfig();
    }

    private void updateConfig() {
        // 获取配置文件实例
        FileConfiguration config = getConfig();

        // 保存刷新项到配置文件中
        List<String> strList = new ArrayList<>();
        for (RefreshItem item : refreshItems) {
            String locationString = item.getLocation().getWorld().getName() + "," +
                    item.getLocation().getBlockX() + "," +
                    item.getLocation().getBlockY() + "," +
                    item.getLocation().getBlockZ();
            try {
                strList.add(locationString + "," +
                        ItemStackUtil.serializeItemStack(item.getItemStack()) + "," +
                        item.getRefreshTime() + "," +
                        item.getRefreshProbability());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        config.set("refreshItems", strList);
        saveConfig();
    }


    private String locationToString(Location location) {
        return location.getWorld().getName() + "," +
                location.getBlockX() + "," +
                location.getBlockY() + "," +
                location.getBlockZ();
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
        private void refreshItems() {
            // 获取当前时间戳
            long currentTimestamp = System.currentTimeMillis();

            // 遍历刷新项列表
            for (RefreshItem item : refreshItems) {
                // 获取上次刷新时间戳
                long lastRefreshTimestamp = item.getLastRefreshTimestamp();

                // 计算距离上次刷新的时间间隔
                long timeInterval = currentTimestamp - lastRefreshTimestamp;

                // 如果时间间隔大于等于刷新时间，则进行刷新
                if (timeInterval >= item.getRefreshTime() * 1000L) {
                    // 获取刷新概率
                    int refreshProbability = item.getRefreshProbability();

                    // 生成随机数
                    int randomNumber = random.nextInt(100);

                    // 判断随机数是否小于等于刷新概率
                    if (randomNumber <= refreshProbability) {
                        // 进行刷新
                        Location location = item.getLocation();
                        ItemStack itemStack = item.getItemStack();
                        BlockState blockState = location.getBlock().getState();

                        if (blockState instanceof Chest) {
                            Chest chest = (Chest) blockState;
                            Inventory inventory = chest.getInventory();

                            // 获取所有空闲格子
                            List<Integer> emptySlots = new ArrayList<>();
                            for (int i = 0; i < inventory.getSize(); i++) {
                                ItemStack item2 = inventory.getItem(i);
                                if (item2 == null || item2.getType() == Material.AIR) {
                                    emptySlots.add(i);
                                }
                            }

                            // 随机选择一个空闲格子
                            if (!emptySlots.isEmpty()) {
                                int index = emptySlots.get(random.nextInt(emptySlots.size()));
                                inventory.setItem(index, itemStack);
                            }
                        }

                        Bukkit.getConsoleSender().sendMessage(itemStack.getType().name() + " was been refreshed at " + location.toString());
                        // 更新刷新时间戳
                        item.setLastRefreshTimestamp(currentTimestamp);
                    }
                }
            }
        }

}

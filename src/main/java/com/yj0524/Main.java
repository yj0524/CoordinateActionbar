package com.yj0524;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public final class Main extends JavaPlugin implements Listener {

    String coordinateMessage;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin Enabled");

        // Config.yml 파일 생성
        loadConfig();
        File cfile = new File(getDataFolder(), "config.yml");
        if (cfile.length() == 0) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        // Register the listener
        getServer().getPluginManager().registerEvents(this, this);

        // Start a repeating task to update the action bar
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    String coords = coordinateMessage.replace("&", "§").replace("%z%", String.valueOf(player.getLocation().getBlockZ())).replace("%y%", String.valueOf(player.getLocation().getBlockY())).replace("%x%", String.valueOf(player.getLocation().getBlockX()));
                    player.sendActionBar(coords);
                }
            }
        }.runTaskTimer(this, 0, 1); // Repeat every 1 ticks (0.05s)
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin Disabled");
    }

    private void loadConfig() {
        // Load chest size from config
        FileConfiguration config = getConfig();
        coordinateMessage = config.getString("coordinateMessage", "&eX&r : %x% | &aY&r : %y% | &bZ&r : %z%");
        // Save config
        config.set("coordinateMessage", coordinateMessage);
        saveConfig();
    }
}

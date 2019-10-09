package io.github.itscryne.zone2;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;

/**
 * @author Patrick Longer
 * @version 1.0-SNAPSHOT
 */
public final class Zone2 extends JavaPlugin implements Serializable {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("createPlayerZone").setExecutor(new Zone2CommandExecutor(this));
        this.getCommand("getPlayerZone").setExecutor(new Zone2CommandExecutor(this));
        this.getCommand("deletePlayerZone").setExecutor(new Zone2CommandExecutor(this));
        this.getCommand("createServerZone").setExecutor(new Zone2CommandExecutor(this));
        this.getCommand("getServerZone").setExecutor(new Zone2CommandExecutor(this));
        this.getCommand("deleteServerZone").setExecutor(new Zone2CommandExecutor(this));

        Bukkit.getPluginManager().registerEvents(new Zone2EventListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

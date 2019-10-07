package io.github.itscryne.zone2;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Zone2 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("test").setExecutor(new Zone2CommandExecutor(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

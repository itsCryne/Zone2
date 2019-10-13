package io.github.itscryne.zone2;

import io.github.itscryne.zone2.commands.Zone2CreateServerZoneCommand;
import io.github.itscryne.zone2.commands.Zone2CreateZoneCommand;
import io.github.itscryne.zone2.events.Zone2EventListener;
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
        this.getCommand("createzone").setExecutor(new Zone2CreateZoneCommand(this));
        this.getCommand("createserverzone").setExecutor(new Zone2CreateServerZoneCommand(this));

        Bukkit.getPluginManager().registerEvents(new Zone2EventListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

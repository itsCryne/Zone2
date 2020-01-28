package io.github.itscryne.zone2;

import io.github.itscryne.zone2.commands.Zone2CreateServerZoneCommand;
import io.github.itscryne.zone2.commands.Zone2CreateZoneCommand;
import io.github.itscryne.zone2.commands.Zone2MyZonesCommandExecutor;
import io.github.itscryne.zone2.commands.Zone2PermissionCommand;
import io.github.itscryne.zone2.events.Zone2BreakTypeEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.io.Serializable;

/**
 * @author Patrick Longer
 * @version 1.0-SNAPSHOT
 */
public final class Zone2 extends JavaPlugin implements Serializable {
    public Zone2() {
        super();
    }

    protected Zone2(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
    {
        super(loader, description, dataFolder, file);
    }

    /**
     * Called when this plugin is enabled
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("createzone").setExecutor(new Zone2CreateZoneCommand(this));
        this.getCommand("createserverzone").setExecutor(new Zone2CreateServerZoneCommand(this));
        this.getCommand("permission").setExecutor(new Zone2PermissionCommand(this));
        this.getCommand("myzones").setExecutor(new Zone2MyZonesCommandExecutor(this));

        Bukkit.getPluginManager().registerEvents(new Zone2BreakTypeEvent(this), this);

    }

    /**
     * Called when this plugin is disabled
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

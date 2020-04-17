package io.github.itscryne.zone2;

import io.github.itscryne.zone2.commands.Zone2CreateServerZoneCommand;
import io.github.itscryne.zone2.commands.Zone2CreateSubZoneCommand;
import io.github.itscryne.zone2.commands.Zone2DeleteSubZoneCommand;
import io.github.itscryne.zone2.commands.Zone2InfoCommand;
import io.github.itscryne.zone2.commands.Zone2CreatePlayerZoneCommand;
import io.github.itscryne.zone2.commands.Zone2MyZonesCommand;
import io.github.itscryne.zone2.commands.Zone2PermissionCommand;
import io.github.itscryne.zone2.commands.Zone2ServerPermissionCommand;
import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.events.Zone2BuildTypeEvent;
import io.github.itscryne.zone2.events.Zone2DestroyTypeEvent;
import io.github.itscryne.zone2.events.Zone2DoorsTypeEvent;
import io.github.itscryne.zone2.events.Zone2EntityTypeEvent;
import io.github.itscryne.zone2.events.Zone2EventListener;
import io.github.itscryne.zone2.events.Zone2InventoryTypeEvent;
import io.github.itscryne.zone2.events.Zone2RedstoneTypeEvent;
import io.github.itscryne.zone2.events.Zone2TransportTypeEvent;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.ServerZone;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.MarkerSet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;

/**
 * @author Patrick Longer
 * @version 1.0-SNAPSHOT
 */
public final class Zone2 extends JavaPlugin implements Serializable {
    private static Zone2 plugin;
    private static MarkerSet markers;
    private static MarkerSet serverMarkers;

    public Zone2() {
        super();
    }

    protected Zone2(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    public static Zone2 getPlugin() {
        return plugin;
    }

    public static MarkerSet getMarkers() {
        return markers;
    }

    public static MarkerSet getServerMarkers() {
        return serverMarkers;
    }

    /**
     * Called when this plugin is enabled
     */
    @Override
    public void onEnable() {
        // TODO: Add remaining JavaDocs
        // TODO: Max area for player
        // TODO: Player can have multiple Zones?
        plugin = this;
        plugin.saveDefaultConfig();

        // Plugin startup logic
        this.getCommand("createzone").setExecutor(new Zone2CreatePlayerZoneCommand());
        this.getCommand("createserverzone").setExecutor(new Zone2CreateServerZoneCommand());
        this.getCommand("permission").setExecutor(new Zone2PermissionCommand());
        this.getCommand("myzones").setExecutor(new Zone2MyZonesCommand());
        this.getCommand("createsubzone").setExecutor(new Zone2CreateSubZoneCommand());
        this.getCommand("zoneinfo").setExecutor(new Zone2InfoCommand());
        this.getCommand("serverpermission").setExecutor(new Zone2ServerPermissionCommand());
        this.getCommand("deletesubzone").setExecutor(new Zone2DeleteSubZoneCommand());

        Bukkit.getPluginManager().registerEvents(new Zone2EventListener(), this);
        Bukkit.getPluginManager().registerEvents(new Zone2DestroyTypeEvent(), this);
        Bukkit.getPluginManager().registerEvents(new Zone2BuildTypeEvent(), this);
        Bukkit.getPluginManager().registerEvents(new Zone2DoorsTypeEvent(), this);
        Bukkit.getPluginManager().registerEvents(new Zone2EntityTypeEvent(), this);
        Bukkit.getPluginManager().registerEvents(new Zone2InventoryTypeEvent(), this);
        Bukkit.getPluginManager().registerEvents(new Zone2RedstoneTypeEvent(), this);
        Bukkit.getPluginManager().registerEvents(new Zone2TransportTypeEvent(), this);

        if (getServer().getPluginManager().getPlugin("dynmap") == null) {
            this.getLogger().warning("Dynmap not found. Not enabling compat");
            return;
        }
        this.getLogger().warning("Dynmap found, Zones will be displayed on it");
        Plugin dynmap = getServer().getPluginManager().getPlugin("dynmap");
        DynmapAPI api = (DynmapAPI) dynmap;

        markers = api.getMarkerAPI().createMarkerSet("zonen", "Zonen", null, false);
        serverMarkers = api.getMarkerAPI().createMarkerSet("serverzonen", "Serverzonen", null, false);

        ConfigReader reader = null;
        try {
            reader = ConfigReader.getInstance();
            for (PlayerZone zone : reader.getPlayerZoneList()) {
                zone.displayAreaMarker(markers);
            }

            for (ServerZone zone : reader.getServerZoneList()) {
                zone.displayAreaMarker(serverMarkers);
            }
        } catch (IOException e) {
            this.getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
        } finally {
            if (reader != null) {
                ConfigReader.destroy();
            }
        }
    }

    /**
     * Called when this plugin is disabled
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

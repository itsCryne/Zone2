package io.github.itscryne.zone2;

import io.github.itscryne.zone2.commands.*;
import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.events.*;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.ServerZone;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.MarkerSet;

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

        this.getLogger().info("Enabling bStats. You can disable it in the bStats configuration file.");
        final int pluginId = 7502;
        Metrics metrics = new Metrics(this, pluginId);

        if (getServer().getPluginManager().getPlugin("dynmap") == null) {
            this.getLogger().warning("Dynmap not found. Not enabling compat");
            return;
        }
        this.getLogger().warning("Dynmap found, Zones will be displayed on it");
        Plugin dynmap = getServer().getPluginManager().getPlugin("dynmap");
        DynmapAPI api = (DynmapAPI) dynmap;

        if (api == null) {
            this.getLogger().severe("Something went wrong while initializing Dynmap compat. Disabling compat.");
            return;
        }

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

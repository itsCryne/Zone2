package io.github.itscryne.zone2.spaces;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.extensions.ZLocation;
import io.github.itscryne.zone2.perms.Permission;
import org.bukkit.Location;
import org.bukkit.World;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @serial JSON
 */
public class Zone extends Area {
    private final int priority;
    private final int id;
    private final UUID zoneUUID;
    private final String name;
    private List<Permission> perms;

    /**
     * @param hx       Hgher x coordinate
     * @param lx       Lower x coordinate
     * @param hy       Higher y coordinate
     * @param ly       Lower l coordinate
     * @param hz       Higher z coordinate
     * @param lz       Lower z coordinate
     * @param w        World
     * @param priority Priority of the Zone
     * @param id       ID of the Zone
     * @param name     Name of the zone
     * @param perms    Permissions of the zone
     */
    protected Zone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id, String name,
                   List<Permission> perms) {
        super(hx, lx, hy, ly, hz, lz, w); // -> l1, l2
        this.id = id;
        this.priority = priority;
        this.zoneUUID = UUID.randomUUID();
        this.name = name;
        this.perms = perms;
    }

    /**
     * Returns the next available ID
     *
     * @return the ID
     * @throws IOException if it cant find a file et al
     */
    public static int getNextId() throws IOException {
        ConfigReader reader = ConfigReader.getInstance();
        List<ServerZone> serverZoneList = reader.getServerZoneList();
        List<PlayerZone> playerZoneList = reader.getPlayerZoneList();
        List<SubZone> subZoneList = reader.getSubZoneList();
        if ((serverZoneList == null || serverZoneList.isEmpty()) && playerZoneList.isEmpty() && (subZoneList == null || subZoneList.isEmpty())) {
            return 0;
        }

        List<Integer> zoneIds = new ArrayList<>();
        if (serverZoneList != null && !serverZoneList.isEmpty()) {
            serverZoneList.forEach(element -> zoneIds.add(element.getId()));
        }
        if (!playerZoneList.isEmpty()) {
            playerZoneList.forEach(element -> zoneIds.add(element.getId()));
        }
        if (subZoneList != null && !subZoneList.isEmpty()) {
            subZoneList.forEach(element -> zoneIds.add(element.getId()));
        }

        if (!zoneIds.isEmpty()) {
            return Collections.max(zoneIds) + 1;
        }
        return 0;
    }

    /**
     * Checks wether the instance Zone collides with any other Zone
     *
     * @return wether the instance collides
     * @throws IOException if it cant find a file et al
     */
    public boolean collidesWithAnyZone() throws IOException {
        ConfigReader reader = ConfigReader.getInstance();
        List<PlayerZone> playerZoneList = reader.getPlayerZoneList();
        List<ServerZone> serverZoneList = reader.getServerZoneList();

        if (!playerZoneList.isEmpty()) {
            for (PlayerZone i : playerZoneList) {
                i.setL1(new ZLocation(Location.deserialize(i.getSerL1())));
                i.setL2(new ZLocation(Location.deserialize(i.getSerL2())));

                if (this.getL2().getX() <= i.getL1().getX() && this.getL1().getX() >= i.getL2().getX()
                        && this.getL2().getY() <= i.getL1().getY() && this.getL1().getY() >= i.getL2().getY()
                        && this.getL2().getZ() <= i.getL1().getZ() && this.getL1().getZ() >= i.getL2().getZ()
                        && this.getL1().getWorld().equals(i.getL1().getWorld())) {
                    return true;

                }
            }
        }

        if (serverZoneList != null && !serverZoneList.isEmpty()) {
            for (ServerZone i : serverZoneList) {
                i.setL1(new ZLocation(Location.deserialize(i.getSerL1())));
                i.setL2(new ZLocation(Location.deserialize(i.getSerL2())));

                if (this.getL2().getX() <= i.getL1().getX() && this.getL1().getX() >= i.getL2().getX()
                        && this.getL2().getY() <= i.getL1().getY() && this.getL1().getY() >= i.getL2().getY()
                        && this.getL2().getZ() <= i.getL1().getZ() && this.getL1().getZ() >= i.getL2().getZ()
                        && this.getL1().getWorld().equals(i.getL1().getWorld())) {
                    return true;

                }
            }
        }
        return false;
    }

    public List<SubZone> getSubZones() throws IOException {
        ConfigReader reader = ConfigReader.getInstance();
        List<SubZone> subZoneList = reader.getSubZoneList();
        List<SubZone> matches = new ArrayList<>();
        for (SubZone zone : subZoneList) {
            if (zone.getSuperZoneUUID().equals(this.getZoneUUID())) {
                matches.add(zone);
            }
        }
        return matches;
    }

    public void displayAreaMarker(MarkerSet markerSet) {
        // https://www.programcreek.com/java-api-examples/?api=org.dynmap.markers.AreaMarker
        // - Example 29
        final String id = this.getName().concat("_").concat(String.valueOf(this.getId()));
        final String name = this.getName();
        final boolean markup = false;
        final String world = this.getL1().getWorld().getName();

        final double[] x = new double[4];
        final double[] z = new double[4];
        x[0] = x[1] = this.getMinX();
        x[2] = x[3] = this.getMaxX();
        z[0] = z[3] = this.getMinZ();
        z[1] = z[2] = this.getMaxZ();

        final boolean persistent = false;

        final AreaMarker marker = markerSet.createAreaMarker(id, name, markup, world, x, z, persistent);

        if (this instanceof ServerZone && marker != null) {
            // thickness, opacity, color
            marker.setLineStyle(1, 0.25, 0x00008b);
            // opacity, color
            marker.setFillStyle(0.5, 0x00008b);
        } else if (this instanceof PlayerZone && marker != null) {
            // thickness, opacity, color
            marker.setLineStyle(1, 0.25, 0xb33ff0);
            // opacity, color
            marker.setFillStyle(0.5, 0xb33ff0);
        } else {
            Zone2.getPlugin().getLogger().severe("Failed to create AreaMarker with id " + id);
        }
    }

    /**
     * @return Priority of the Zone
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * @return ID of the Zone
     */
    public int getId() {
        return this.id;
    }

    public UUID getZoneUUID() {
        return this.zoneUUID;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Zone zone) {
        return this.getZoneUUID().equals(zone.getZoneUUID());
    }

    /**
     * @return Permissions of the zone
     */
    public List<Permission> getPerms() {
        return this.perms;
    }

    public void setPerms(List<Permission> perms) {
        this.perms = perms;
    }
}

package io.github.itscryne.zone2.spaces;

import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;
import java.util.List;

/**
 * @serial JSON
 */
public class Zone extends Area implements Serializable {
    private int priority;
    private int id;

    /**
     * @param l1       First Location (Higher Coordinates)
     * @param l2       Second Location (Lower Coordinates)
     * @param priority Priority of the zone
     * @param id       ID of the zone
     */
    protected Zone(Location l1, Location l2, int priority, int id) {
        super(l1, l2); // -> l1, l2
        this.id = id;
        this.priority = priority;
    }

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
     */
    protected Zone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id) {
        super(hx, lx, hy, ly, hz, lz, w); // -> l1, l2
        this.id = id;
        this.priority = priority;
    }

    public static ServerZone getHighestServerZonePriority(List<ServerZone> zoneList) {
        if (zoneList.isEmpty()) {
            throw new RuntimeException("The zoneList must not be empty");
        }

        ServerZone highestPriorityZone = null;

        for (ServerZone z : zoneList) {
            if (highestPriorityZone == null) {
                highestPriorityZone = z;
            }
            if (highestPriorityZone.getPriority() < z.getPriority()) {
                highestPriorityZone = z;
            }
        }
        return highestPriorityZone;
    }

    public static PlayerZone getHighestPlayerZonePriority(List<PlayerZone> zoneList) {
        if (zoneList.isEmpty()) {
            throw new RuntimeException("The zoneList must not be empty");
        }

        PlayerZone highestPriorityZone = null;

        for (PlayerZone z : zoneList) {
            if (highestPriorityZone == null) {
                highestPriorityZone = z;
            }
            if (highestPriorityZone.getPriority() < z.getPriority()) {
                highestPriorityZone = z;
            }
        }
        return highestPriorityZone;
    }

    public static Zone getHighestPriorityZone(List<PlayerZone> playerZoneList, List<ServerZone> serverZoneList) {
        if (playerZoneList.isEmpty() && serverZoneList.isEmpty()) {
            throw new RuntimeException("Both Lists are empty. This is forbidden");
        }

        if (playerZoneList.isEmpty()) {
            return Zone.getHighestServerZonePriority(serverZoneList);
        } else if (serverZoneList.isEmpty()) {
            return Zone.getHighestPlayerZonePriority(playerZoneList);
        } else {
            PlayerZone highestPriorityPlayerZone = null;
            ServerZone highestPriorityServerZone = null;

            Zone highestPriorityZone;

            highestPriorityPlayerZone = Zone.getHighestPlayerZonePriority(playerZoneList);
            highestPriorityServerZone = Zone.getHighestServerZonePriority(serverZoneList);

            if (highestPriorityPlayerZone.getPriority() > highestPriorityServerZone.getPriority()) {
                highestPriorityZone = highestPriorityPlayerZone;
            } else {
                highestPriorityZone = highestPriorityServerZone;
            }
            return highestPriorityZone;
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
}

package io.github.itscryne.zone2.spaces;

import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.Zone2;
import org.bukkit.World;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.itscryne.zone2.extensions.Zonecation;
/**
 * @serial JSON
 */
public class ServerZone extends Zone implements Serializable {
    private String name;

    /**
     * Constructor for Zonecations
     * @param l1       First Zonecation (higher coordinates)
     * @param l2       Second Zonecation (lower coordinates)
     * @param priority Priority of the Zone
     * @param id       ID of the Zone
     * @param name     Name of the Zone
     */
    public ServerZone(Zonecation l1, Zonecation l2, int priority, int id, String name) {
        super(l1, l2, priority, id);
        this.name = name;
    }

    /**
     * Constructor for raw coordinates
     * @param hx       Hgher x coordinate
     * @param lx       Lower x coordinate
     * @param hy       Higher y coordinate
     * @param ly       Lower l coordinate
     * @param hz       Higher z coordinate
     * @param lz       Lower z coordinate
     * @param w        World
     * @param priority Priority of the Zone
     * @param id       ID of the Zone
     * @param name     Name of the Zone
     */
    //TODO: Move name to Zone
    public ServerZone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id, String name) {
        super(hx, lx, hy, ly, hz, lz, w, priority, id);
        this.name = name;
    }

    /**
     * Checks wether the instance Zone collides with any other Zone
     * @param plugin the plugin
     * @return wether the instance collides
     * @throws IOException if it cant find a file et al
     */
    public boolean collidesWithAnyZone(Zone2 plugin) throws IOException {
        ConfigReader reader = ConfigReader.getInstance(plugin);
        List<PlayerZone> playerZoneList = reader.getPlayerZoneList();
        List<ServerZone> serverZoneList = reader.getServerZoneList();

        if (playerZoneList != null) {
            if (!playerZoneList.isEmpty()) {
                for (PlayerZone i : playerZoneList) {
                    i.setL1((Zonecation) Zonecation.deserialize(i.getSerL1()));
                    i.setL2((Zonecation) Zonecation.deserialize(i.getSerL2()));

                    if ((this.getL2().getX() <= i.getL1().getX() && this.getL1().getX() >= i.getL2().getX())
                            && (this.getL2().getY() <= i.getL1().getY() && this.getL1().getY() >= i.getL2().getY())
                            && (this.getL2().getZ() <= i.getL1().getZ() && this.getL1().getZ() >= i.getL2().getZ())
                            && (this.getL1().getWorld().equals(i.getL1().getWorld()))) { //TODO does this really work like intended?
                        return true;
                    }
                }
            }
        }

        if (playerZoneList != null) {
            if (!playerZoneList.isEmpty()) {
                for (ServerZone i : serverZoneList) {
                    i.setL1((Zonecation) Zonecation.deserialize(i.getSerL1()));
                    i.setL2((Zonecation) Zonecation.deserialize(i.getSerL2()));

                    if ((this.getL2().getX() <= i.getL1().getX() && this.getL1().getX() >= i.getL2().getX())
                            && (this.getL2().getY() <= i.getL1().getY() && this.getL1().getY() >= i.getL2().getY())
                            && (this.getL2().getZ() <= i.getL1().getZ() && this.getL1().getZ() >= i.getL2().getZ())
                            && (this.getL1().getWorld().equals(i.getL1().getWorld()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns the next available ID
     * @param plugin the plugin
     * @return the ID
     * @throws IOException if it cant find a file et al
     */
    public static int getNextId(Zone2 plugin) throws IOException {//TODO: -> Zone
        ConfigReader reader = ConfigReader.getInstance(plugin);
        List<ServerZone> serverZoneList = reader.getServerZoneList();

        if (serverZoneList == null || serverZoneList.isEmpty()){
            return 0;
        }

        List<Integer> zoneIds = new ArrayList<>();
        serverZoneList.forEach(element -> zoneIds.add(element.getId()));

        return Collections.max(zoneIds) + 1;
    }

    /**
     * @return Name of the zone
     */
    public String getName() {
        return this.name;
    }
}

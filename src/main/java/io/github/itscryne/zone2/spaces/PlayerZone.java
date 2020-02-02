package io.github.itscryne.zone2.spaces;

import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.Zone2;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.github.itscryne.zone2.extensions.Zonecation;

/**
 * @serial JSON
 */
public class PlayerZone extends Zone implements Serializable {
    private String name;
    private UUID playerUuid;
    private List<Permission> perms;

    /**
     * @param hx         Hgher x coordinate
     * @param lx         Lower x coordinate
     * @param hy         Higher y coordinate
     * @param ly         Lower l coordinate
     * @param hz         Higher z coordinate
     * @param lz         Lower z coordinate
     * @param w          World
     * @param priority   Priority of the Zone
     * @param id         ID of the Zone
     * @param name       Name of the Zone
     * @param playerUuid UUID of the creating player
     * @param perms      Permissions to set
     */
    //TODO: Move name to Zone
    public PlayerZone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id, String name, UUID playerUuid, List<Permission> perms) {
        super(hx, lx, hy, ly, hz, lz, w, priority, id);
        this.name = name;
        this.playerUuid = playerUuid;
        this.perms = perms;
    }

    /**
     * @param l1         First Zonecation (higher coordinates)
     * @param l2         Second Zonecation (lower coordinates)
     * @param priority   Priority of the Zone
     * @param id         ID of the Zone
     * @param name       Name of the Zone
     * @param playerUuid UUID of the creating player
     * @param perms      Permissions to set
     */
    //TODO: Move name to Zone
    public PlayerZone(Zonecation l1, Zonecation l2, int priority, int id, String name, UUID playerUuid, List<Permission> perms) {
        super(l1, l2, priority, id);
        this.name = name;
        this.playerUuid = playerUuid;
        this.perms = perms;
    }

    /**
     * Function to check, wether the player already has a Zone
     * @param p the player to check
     * @param plugin the plugin
     * @return wether the player has a zone
     * @throws IOException if it cant find a file et al
     */
    public static boolean hasZone(Player p, Zone2 plugin) throws IOException {
        UUID playerUuid = p.getUniqueId();

        ConfigReader reader = ConfigReader.getInstance(plugin);
        List<PlayerZone> playerZoneList = reader.getPlayerZoneList();
        List<UUID> zoneUuids = new ArrayList<>();
        playerZoneList.forEach(element -> zoneUuids.add(element.playerUuid));

        reader.destroy();
        return !zoneUuids.contains(playerUuid);
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

        if (playerZoneList == null && serverZoneList == null) {
            return false;
        }


        if (playerZoneList != null) {
            if (!playerZoneList.isEmpty()) {
                for (PlayerZone i : playerZoneList) {
                    i.setL1(new Zonecation(Zonecation.deserialize(i.getSerL1())));
                    i.setL2(new Zonecation(Zonecation.deserialize(i.getSerL2())));

                    if ((this.getL2().getX() <= i.getL1().getX() && this.getL1().getX() >= i.getL2().getX())
                            && (this.getL2().getY() <= i.getL1().getY() && this.getL1().getY() >= i.getL2().getY())
                            && (this.getL2().getZ() <= i.getL1().getZ() && this.getL1().getZ() >= i.getL2().getZ())
                            && (this.getL1().getWorld().equals(i.getL1().getWorld()))) {
                        return true;
                    }
                }
            }
        }

        if (playerZoneList != null) {
            if (!playerZoneList.isEmpty()) {
                for (ServerZone i : serverZoneList) {
                    i.setL1(new Zonecation(Zonecation.deserialize(i.getSerL1())));
                    i.setL2(new Zonecation(Zonecation.deserialize(i.getSerL2())));

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
    public static int getNextId(Zone2 plugin) throws IOException { //TODO: -> Zone
        ConfigReader reader = ConfigReader.getInstance(plugin);
        List<PlayerZone> playerZoneList = reader.getPlayerZoneList();

        if (playerZoneList == null || playerZoneList.isEmpty()){
            return 0;
        }

        List<Integer> zoneIds = new ArrayList<>();
        playerZoneList.forEach(element -> zoneIds.add(element.getId()));

        return Collections.max(zoneIds) + 1;
    }

    /**
     * @return Name of the zone
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return UUID of the player the zone belongs to
     */
    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    /**
     * @return Permissions of the zone
     */
    public List<Permission> getPerms() {
        return this.perms;
    }

    public void setPerms(List<Permission> perms) { this.perms = perms; }
}

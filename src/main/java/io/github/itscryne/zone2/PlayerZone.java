package io.github.itscryne.zone2;

import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @serial JSON
 */
public class PlayerZone extends Zone implements Serializable {
    private String name;
    private UUID playerUuid;
    private List<Permission> perms;

    /**
     *
     * @param hx Hgher x coordinate
     * @param lx Lower x coordinate
     * @param hy Higher y coordinate
     * @param ly Lower l coordinate
     * @param hz Higher z coordinate
     * @param lz Lower z coordinate
     * @param w World
     * @param priority Priority of the Zone
     * @param id ID of the Zone
     * @param name Name of the Zone
     * @param playerUuid UUID of the creating player
     * @param perms Permissions to set
     */
    //TODO: Move name to Zone
    protected PlayerZone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id, String name, UUID playerUuid, List<Permission> perms) {
        super(hx, lx, hy, ly, hz, lz, w, priority, id);
        this.name = name;
        this.playerUuid = playerUuid;
        this.perms = perms;
    }

    /**
     *
     * @param l1 First Location (higher coordinates)
     * @param l2 Second Location (lower coordinates)
     * @param priority Priority of the Zone
     * @param id ID of the Zone
     * @param name Name of the Zone
     * @param playerUuid UUID of the creating player
     * @param perms Permissions to set
     */
    //TODO: Move name to Zone
    protected PlayerZone(Location l1, Location l2, int priority, int id, String name, UUID playerUuid, List<Permission> perms) {
        super(l1, l2, priority, id);
        this.name = name;
        this.playerUuid = playerUuid;
        this.perms = perms;
    }

    /**
     *
     * @return Name of the zone
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return UUID of the player the zone belongs to
     */
    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    /**
     *
     * @return Permissions of the zone
     */
    public List<Permission> getPerms() {
        return this.perms;
    }
}

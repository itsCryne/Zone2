package io.github.itscryne.zone2.spaces;

import io.github.itscryne.zone2.perms.Permission;
import org.bukkit.World;

import java.util.List;
import java.util.UUID;

import io.github.itscryne.zone2.extensions.Zonecation;

/**
 * @serial JSON
 */
public class PlayerZone extends Zone {
    private UUID playerUuid;

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

     */
    public PlayerZone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id, String name,
            UUID playerUuid, List<Permission> perms) {
        super(hx, lx, hy, ly, hz, lz, w, priority, id, name, perms);
        this.playerUuid = playerUuid;
    }

    /**
     * @param l1         First Zonecation (higher coordinates)
     * @param l2         Second Zonecation (lower coordinates)
     * @param priority   Priority of the Zone
     * @param id         ID of the Zone
     * @param name       Name of the Zone
     * @param playerUuid UUID of the creating player
     */
    public PlayerZone(Zonecation l1, Zonecation l2, int priority, int id, String name, UUID playerUuid,
            List<Permission> perms) {
        super(l1, l2, priority, id, name, perms);
        this.playerUuid = playerUuid;
    }

    /**
     * @return UUID of the player the zone belongs to
     */
    public UUID getPlayerUuid() {
        return this.playerUuid;
    }
}

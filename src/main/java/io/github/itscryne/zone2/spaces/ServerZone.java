package io.github.itscryne.zone2.spaces;

import java.util.List;

import org.bukkit.World;

import io.github.itscryne.zone2.extensions.Zonecation;
import io.github.itscryne.zone2.perms.Permission;

/**
 * @serial JSON
 */
public class ServerZone extends Zone {

    /**
     * Constructor for Zonecations
     * 
     * @param l1       First Zonecation (higher coordinates)
     * @param l2       Second Zonecation (lower coordinates)
     * @param priority Priority of the Zone
     * @param id       ID of the Zone
     * @param name     Name of the Zone
     */
    public ServerZone(Zonecation l1, Zonecation l2, int priority, int id, String name, List<Permission> perms) {
        super(l1, l2, priority, id, name, perms);
    }

    /**
     * Constructor for raw coordinates
     * 
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
    public ServerZone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id, String name, List<Permission> perms) {
        super(hx, lx, hy, ly, hz, lz, w, priority, id, name, perms);
    }
}

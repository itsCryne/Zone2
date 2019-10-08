package io.github.itscryne.zone2;

import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;

/**
 * @serial JSON
 */
public class Zone extends Area implements Serializable {
    private int priority;
    private int id;

    /**
     *
     * @param l1 First Location (Higher Coordinates)
     * @param l2 Second Location (Lower Coordinates)
     * @param priority Priority of the zone
     * @param id ID of the zone
     */
    protected Zone(Location l1, Location l2, int priority, int id) {
        super(l1, l2); // -> l1, l2
        this.id = id;
        this.priority = priority;
    }

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
     */
    protected Zone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id) {
        super(hx, lx, hy, ly, hz, lz, w); // -> l1, l2
        this.id = id;
        this.priority = priority;
    }

    /**
     *
     * @return Priority of the Zone
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     *
     * @return ID of the Zone
     */
    public int getId() {
        return this.id;
    }
}

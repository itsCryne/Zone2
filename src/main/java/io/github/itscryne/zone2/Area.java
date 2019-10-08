package io.github.itscryne.zone2;

import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;
import java.util.Map;

/**
 * @serial JSON
 */
public class Area implements Serializable {
    private transient Location l1; //upper location
    private transient Location l2; //lower location
    private Map<String, Object> serL1;
    private Map<String, Object> serL2;

    /**
     *
     * @param l1 First Location (higher coordinates)
     * @param l2 Second Location (lower coordinates)
     */
    protected Area(Location l1, Location l2){
        this.l1 = l1;
        this.l2 = l2;
        this.serL1 = this.l1.serialize();
        this.serL2 = this.l2.serialize();
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
     */
    protected Area(int hx, int lx, int hy, int ly, int hz, int lz, World w){
        this.l1 = new Location(w, hx, hy, hz);
        this.l2 = new Location(w, lx, ly, lz);
        this.serL1 = this.l1.serialize();
        this.serL2 = this.l2.serialize();
    }

    /**
     *
     * @return First Location (higher coordinates)
     */
    public Location getL1() {
        return this.l1;
    }

    /**
     *
     * @return Second Location (lower coordinates)
     */
    public Location getL2() {
        return this.l2;
    }
}

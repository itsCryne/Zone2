package io.github.itscryne.zone2.spaces;

import org.bukkit.World;

import java.io.Serializable;
import java.util.Map;

import io.github.itscryne.zone2.extensions.Zonecation;

/**
 * @serial JSON
 */
public class Area implements Serializable {
    private transient Zonecation l1; //upper Location
    private transient Zonecation l2; //lower Location
    private Map<String, Object> serL1;
    private Map<String, Object> serL2;

    /**
     * @param l1 First Location (higher coordinates)
     * @param l2 Second Location (lower coordinates)
     */
    protected Area(Zonecation l1, Zonecation l2) {
        this.l1 = l1;
        this.l2 = l2;
        this.serL1 = this.l1.serialize();
        this.serL2 = this.l2.serialize();

        if (!(this.l1.getWorld().equals(this.l2.getWorld()))) {
            throw new RuntimeException("Worlds do not match");
        }

        if (this.l1.getBlockX() < this.l2.getBlockX()) {
            throw new RuntimeException("l1 must contain the higher coordinates");
        } else if (this.l1.getBlockY() < this.l1.getBlockY()) {
            throw new RuntimeException("l1 must conatin the higher coordinates");
        } else if (this.l1.getBlockZ() < this.l2.getBlockZ()) {
            throw new RuntimeException("l1 must contain the higher coordinates");
        }

        if (this.l1.getBlockX() == this.l2.getBlockX()) {
            throw new RuntimeException("This is not an area");
        } else if (this.l1.getBlockY() == this.l2.getBlockY()) {
            throw new RuntimeException("This is not an area");
        } else if (this.l1.getBlockY() == this.l2.getBlockY()) {
            throw new RuntimeException("This is not an area");
        }
    }

    /**
     * @param hx Hgher x coordinate
     * @param lx Lower x coordinate
     * @param hy Higher y coordinate
     * @param ly Lower l coordinate
     * @param hz Higher z coordinate
     * @param lz Lower z coordinate
     * @param w  World
     */
    protected Area(int hx, int lx, int hy, int ly, int hz, int lz, World w) {
        this.l1 = new Zonecation(w, hx, hy, hz);
        this.l2 = new Zonecation(w, lx, ly, lz);
        this.serL1 = this.l1.serialize();
        this.serL2 = this.l2.serialize();

        if (this.l1.getBlockX() < this.l2.getBlockX()) {
            throw new RuntimeException("l1 must contain the higher coordinates");
        } else if (this.l1.getBlockY() < this.l1.getBlockY()) {
            throw new RuntimeException("l1 must conatin the higher coordinates");
        } else if (this.l1.getBlockZ() < this.l2.getBlockZ()) {
            throw new RuntimeException("l1 must contain the higher coordinates");
        }
    }

    /**
     * @param l Location to test
     * @return Wether the Zone object contains l
     */
    public boolean contains(Zonecation l) {
        if (this.l1 == null) {
            this.l1 = (Zonecation) Zonecation.deserialize(serL1);
        }
        if (this.l2 == null) {
            this.l2 = (Zonecation) Zonecation.deserialize(serL2);
        }

        boolean worldsMatch = l.getWorld().equals(this.getL1().getWorld());
        boolean xContain = l.getBlockX() < this.l1.getBlockX() && l.getBlockX() > this.l2.getBlockX();
        boolean yContain = l.getBlockY() < this.l1.getBlockY() && l.getBlockY() > this.l2.getBlockY();
        boolean zContain = l.getBlockZ() < this.l1.getBlockZ() && l.getBlockZ() > this.l2.getBlockZ();

        return xContain && yContain && zContain && worldsMatch;
    }

    /**
     * @return First Location (higher coordinates)
     */
    public Zonecation getL1() {
        return this.l1;
    }

    /**
     *
     * @param l1 The Location to set
     */
    public void setL1(Zonecation l1) {
        this.l1 = l1;
    }

    /**
     * @return Second Location (lower coordinates)
     */
    public Zonecation getL2() {
        return this.l2;
    }

    /**
     *
     * @param l2 The Location to set
     */
    public void setL2(Zonecation l2) {
        this.l2 = l2;
    }

    /**
     *
     * @return the serialized L1
     */
    public Map<String, Object> getSerL1() {
        return serL1;
    }

    /**
     *
     * @return the serialized L2
     */
    public Map<String, Object> getSerL2() {
        return serL2;
    }
}

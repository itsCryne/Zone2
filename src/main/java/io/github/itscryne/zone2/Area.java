package io.github.itscryne.zone2;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Map;

public class Area{
    private transient Location l1; //upper location
    private transient Location l2; //lower location
    private Map<String, Object> serL1;
    private Map<String, Object> serL2;

    //Constructors
    protected Area(Location l1, Location l2){
        this.l1 = l1;
        this.l2 = l2;
        this.serL1 = this.l1.serialize();
        this.serL2 = this.l2.serialize();
    }
    protected Area(int hx, int lx, int hy, int ly, int hz, int lz, World w){
        this.l1 = new Location(w, hx, hy, hz);
        this.l2 = new Location(w, lx, ly, lz);
        this.serL1 = this.l1.serialize();
        this.serL2 = this.l2.serialize();
    }

    //Getters
    public Location getL1() {
        return this.l1;
    }
    public Location getL2() {
        return this.l2;
    }
}

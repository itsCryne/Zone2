package io.github.itscryne.zone2;

import org.bukkit.Location;
import org.bukkit.World;

public class Area{
    private Location l1; //upper location
    private Location l2; //lower location

    //Constructors
    protected Area(Location l1, Location l2){
        this.l1 = l1;
        this.l2 = l2;
    }
    protected Area(int hx, int lx, int hy, int ly, int hz, int lz, World w){
        this.l1 = new Location(w, hx, hy, hz);
        this.l2 = new Location(w, lx, ly, lz);
    }

    //Getters
    public Location getL1() {
        return this.l1;
    }
    public Location getL2() {
        return this.l2;
    }
}

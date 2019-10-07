package io.github.itscryne.zone2;

import org.bukkit.Location;
import org.bukkit.World;

public class Zone extends Area{
    private int priority;
    private int id;

    //Constructor
    protected Zone(Location l1, Location l2, int priority, int id) {
        super(l1, l2); // -> l1, l2
        this.id = id;
        this.priority = priority;
    }

    protected Zone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id) {
        super(hx, lx, hy, ly, hz, lz, w); // -> l1, l2
        this.id = id;
        this.priority = priority;
    }

    //Getters
    public int getPriority() {
        return this.priority;
    }
    public int getId() {
        return this.id;
    }
}

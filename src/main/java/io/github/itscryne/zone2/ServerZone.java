package io.github.itscryne.zone2;

import org.bukkit.Location;
import org.bukkit.World;

public class ServerZone extends Zone {
    private String name;

    protected ServerZone(Location l1, Location l2, int priority, int id, String name) {
        super(l1, l2, priority, id);
        this.name = name;
    }

    protected ServerZone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id, String name) {
        super(hx, lx, hy, ly, hz, lz, w, priority, id);
        this.name = name;
    }

    //Getters
    public String getName() {
        return this.name;
    }
}

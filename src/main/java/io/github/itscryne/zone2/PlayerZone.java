package io.github.itscryne.zone2;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.UUID;

public class PlayerZone extends Zone{
    private String name;
    private UUID playerUuid;
    private List<Permission> perms;

    //Constructor
    protected PlayerZone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id, String name, UUID playerUuid, List<Permission> perms) {
        super(hx, lx, hy, ly, hz, lz, w, priority, id);
        this.name = name;
        this.playerUuid = playerUuid;
        this.perms = perms;
    }
    protected PlayerZone(Location l1, Location l2, int priority, int id, String name, UUID playerUuid, List<Permission> perms) {
        super(l1, l2, priority, id);
        this.name = name;
        this.playerUuid = playerUuid;
        this.perms = perms;
    }

    //Getters
    public String getName() {
        return this.name;
    }
    public UUID getPlayerUuid() {
        return this.playerUuid;
    }
    public List<Permission> getPerms() {
        return this.perms;
    }
}

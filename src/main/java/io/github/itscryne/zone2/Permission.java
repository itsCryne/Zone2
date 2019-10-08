package io.github.itscryne.zone2;

import org.bukkit.entity.Player;

import java.util.UUID;

public class Permission {
    private UUID playerUuid;
    private transient Player p;
    private PermissionType perm;

    //Constructor
    public Permission(Player p, PermissionType perm){
        this.p = p;
        this.playerUuid = p.getUniqueId();
        this.perm = perm;
    }

    //Getters
    public PermissionType getPerm() {
        return this.perm;
    }

    public UUID getPlayerUuid() { return playerUuid; }

    public Player getP() { return p; }
}

package io.github.itscryne.zone2;

import org.bukkit.entity.Player;

public class Permission {
    private Player p;
    private PermissionType perm;

    //Constructor
    public Permission(Player p, PermissionType perm){
        this.p = p;
        this.perm = perm;
    }

    //Getters
    public PermissionType getPerm() {
        return this.perm;
    }
    public Player getP() {
        return this.p;
    }
}

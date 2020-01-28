package io.github.itscryne.zone2.perms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.UUID;

/**
 * @serial JSON
 */
public class Permission implements Serializable {
    private UUID playerUuid;
    private transient Player p;
    private PermissionType perm;

    /**
     * @param p    Player the permission belongs to
     * @param perm Type of the permission
     */
    public Permission(Player p, PermissionType perm) {
        this.p = p;
        this.playerUuid = p.getUniqueId();
        this.perm = perm;
    }

    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }

        Permission permObj = (Permission) obj;

        boolean uuidEquals = this.getPlayerUuid().equals(permObj.getPlayerUuid());
        boolean playerEquals = this.getP().equals(permObj.getP());
        boolean permissionTypeEquals = this.getPerm().equals(permObj.getPerm());

        return uuidEquals && playerEquals && permissionTypeEquals;
    }

    /**
     * @return Type of the permission
     */
    public PermissionType getPerm() {
        return this.perm;
    }

    /**
     * @return UUID of the player the permission belongs to
     */
    public UUID getPlayerUuid() {
        return playerUuid;
    }

    /**
     * @return Player the permission belongs to
     */
    public Player getP() {
        if (this.p == null){
            this.p = Bukkit.getPlayer(this.playerUuid);
            if (this.p == null){
                throw (new RuntimeException("Failed to deserialize player"));
            }
        }
        return this.p;
    }
}

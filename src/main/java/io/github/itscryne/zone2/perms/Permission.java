package io.github.itscryne.zone2.perms;

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
        return p;
    }
}

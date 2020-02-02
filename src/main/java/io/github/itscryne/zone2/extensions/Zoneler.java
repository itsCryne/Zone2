package io.github.itscryne.zone2.extensions;

import java.io.IOException;
import java.util.List;

import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.perms.PermissionType;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.ServerZone;
import io.github.itscryne.zone2.spaces.Zone;
import net.minecraft.server.v1_14_R1.EntityPlayer;

public class Zoneler extends CraftPlayer {

    public Zoneler(CraftServer server, EntityPlayer entity) {
        super(server, entity);
    }

    public Zoneler(Player p){
        super((CraftServer) p.getServer(), (EntityPlayer) p); //Does this work?
    }

        /**
     *
     * @param l Zonecation to check
     * @param t PermissionType to check
     * @return wether the player is allowed to do the action
     * @throws IOException if it cant access a file et al
     */
    public boolean isAllowed(Zonecation l, PermissionType t) throws IOException {
        boolean inPlayerZone = l.inPlayerZone();
        boolean inServerZone = l.inServerZone();
        boolean inZone = l.inZone();

        List<PlayerZone> playerZoneList = l.getPlayerZones();
        List<ServerZone> serverZoneList = l.getServerZones();

        if (!inZone){
            return this.hasPermission("Zone2.modifyNoZone");
        }
        if (inPlayerZone && inServerZone){
            Zone highestPriority = Zone.getHighestPriorityZone(playerZoneList, serverZoneList);
            if (highestPriority instanceof PlayerZone){
                PlayerZone highestPriorityPlayerZone = (PlayerZone) highestPriority;
                Permission neededPerm = new Permission(this, t);
                Permission administratePerm = new Permission(this, PermissionType.MANAGE);
                return highestPriorityPlayerZone.getPerms().contains(neededPerm)
                        || highestPriorityPlayerZone.getPerms().contains(administratePerm)
                        || highestPriorityPlayerZone.getPlayerUuid().equals(this.getUniqueId())
                        || this.hasPermission("Zone2.modifyPlayerZone");
            } else {
                return this.hasPermission("Zone2.modifyServerZone");
            }
        } else if (inPlayerZone){
            PlayerZone highestPriorityPlayerZone = PlayerZone.getHighestPlayerZonePriority(playerZoneList);
            Permission neededPerm = new Permission(this, t);
            Permission administratePerm = new Permission(this, PermissionType.MANAGE);
            return highestPriorityPlayerZone.getPerms().contains(neededPerm)
                    || highestPriorityPlayerZone.getPerms().contains(administratePerm)
                    || highestPriorityPlayerZone.getPlayerUuid().equals(this.getUniqueId())
                    || this.hasPermission("Zone2.modifyPlayerZone");
        } else {
            return this.hasPermission("Zone2.modifyServerZone");
        }
    }
}
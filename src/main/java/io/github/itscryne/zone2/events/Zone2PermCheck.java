package io.github.itscryne.zone2.events;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.perms.PermissionType;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.ServerZone;
import io.github.itscryne.zone2.spaces.Zone;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zone2PermCheck {
    protected static boolean inPlayerZone(Location l, Zone2 plugin) throws FileNotFoundException, IOException {
        boolean inPlayerZone = false;
        List<PlayerZone> playerZoneList = new ArrayList<>();

        for (PlayerZone i : ConfigReader.getInstance(plugin).getPlayerZoneList()){
            if (i.contains(l)){
                playerZoneList.add(i);
                inPlayerZone = true;
            }
        }
        return inPlayerZone;
    }

    protected static boolean inServerZone(Location l, Zone2 plugin) throws FileNotFoundException, IOException {
        boolean inServerZone = false;
        List<ServerZone> serverZoneList = new ArrayList<>();

        for (ServerZone i : ConfigReader.getInstance(plugin).getServerZoneList()){
            if (i.contains(l)){
                serverZoneList.add(i);
                inServerZone = true;
            }
        }
        return inServerZone;
    }

    protected static boolean inZone(Location l, Zone2 plugin) throws FileNotFoundException, IOException {
        return inPlayerZone(l, plugin) || inServerZone(l, plugin);
    }

    /**
     *
     * @param l Location to check
     * @param p Player to chekc
     * @param t PermissionType to check
     * @param plugin the plugin
     * @return wether the player is allowed to do the action
     * @throws IOException if it cant access a file et al
     */
    protected static boolean isAllowed(Location l, Player p, PermissionType t, Zone2 plugin) throws IOException {
        boolean inPlayerZone = inPlayerZone(l, plugin);
        boolean inServerZone = inServerZone(l, plugin);
        boolean inZone = inZone(l, plugin);

        List<PlayerZone> playerZoneList = new ArrayList<>();
        List<ServerZone> serverZoneList = new ArrayList<>();

        if (!inZone){
            return p.hasPermission("Zone2.modifyNoZone");
        }
        if (inPlayerZone && inServerZone){
            Zone highestPriority = Zone.getHighestPriorityZone(playerZoneList, serverZoneList);
            if (highestPriority instanceof PlayerZone){
                PlayerZone highestPriorityPlayerZone = (PlayerZone) highestPriority;
                Permission neededPerm = new Permission(p, t);
                Permission administratePerm = new Permission(p, PermissionType.MANAGE);
                return highestPriorityPlayerZone.getPerms().contains(neededPerm)
                        || highestPriorityPlayerZone.getPerms().contains(administratePerm)
                        || highestPriorityPlayerZone.getPlayerUuid().equals(p.getUniqueId())
                        || p.hasPermission("Zone2.modifyPlayerZone");
            } else {
                return p.hasPermission("Zone2.modifyServerZone");
            }
        } else if (inPlayerZone){
            PlayerZone highestPriorityPlayerZone = PlayerZone.getHighestPlayerZonePriority(playerZoneList);
            Permission neededPerm = new Permission(p, t);
            Permission administratePerm = new Permission(p, PermissionType.MANAGE);
            return highestPriorityPlayerZone.getPerms().contains(neededPerm)
                    || highestPriorityPlayerZone.getPerms().contains(administratePerm)
                    || highestPriorityPlayerZone.getPlayerUuid().equals(p.getUniqueId())
                    || p.hasPermission("Zone2.modifyPlayerZone");
        } else {
            return p.hasPermission("Zone2.modifyServerZone");
        }
    }
}

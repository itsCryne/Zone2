package io.github.itscryne.zone2.extensions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.perms.PermissionType;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.SubZone;
import io.github.itscryne.zone2.spaces.Zone;
import net.minecraft.server.v1_14_R1.ChatMessageType;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;
import net.minecraft.server.v1_14_R1.PlayerConnection;

public class Zoneler extends CraftPlayer {
    static Set<UUID> noNotify;

    public Zoneler(CraftServer server, EntityPlayer entity) {
        super(server, entity);
    }

    public Zoneler(Player p) {
        super((CraftServer) p.getServer(), ((CraftPlayer) p).getHandle()); // Does this work?
    }

    /**
     *
     * @param l Zonecation to check
     * @param t PermissionType to check
     * @return wether the player is allowed to do the action
     * @throws IOException if it cant access a file et al
     */
    @SuppressWarnings({"unchecked","deprecation"})
    public boolean isAllowed(Zonecation l, PermissionType t) throws IOException { // TODO: * perms
        boolean inSubZone = l.inSubZone();
        boolean inZone = l.inZone();

        Permission neededPerm = new Permission(this, t);
        Permission administratePerm = new Permission(this, PermissionType.MANAGE);
        
        OfflinePlayer p = Bukkit.getOfflinePlayer("*");
        Permission genericPerm = new Permission(p, t);

        final List<String> noZoneWorlds = (List<String>) Zone2.getPlugin().getConfig().getList("noZoneWorld");

        if (!inZone) {
            if(noZoneWorlds.contains(l.getWorld().getName())){
                return true;
            } else {
                return this.hasPermission("Zone2.modifyNoZone");
            }
        }

        if (inSubZone) {
            SubZone highestPrioritySubZone = l.getHighestPrioritySubZone();

            return highestPrioritySubZone.getPerms().contains(neededPerm)
                    || highestPrioritySubZone.getPerms().contains(administratePerm)
                    || highestPrioritySubZone.getPlayerUUID().equals(this.getUniqueId()) //TODO subzones for serverzones maybe
                    || this.hasPermission("Zone2.modifyPlayerZone")
                    || highestPrioritySubZone.getPerms().contains(genericPerm);
        } else {
            Zone highestPriorityZone = l.getHighestPriorityZone();

            if(highestPriorityZone instanceof PlayerZone){
                return highestPriorityZone.getPerms().contains(neededPerm)
                        || highestPriorityZone.getPerms().contains(administratePerm)
                        || ((PlayerZone) highestPriorityZone).getPlayerUuid().equals(this.getUniqueId())
                        || this.hasPermission("Zone2.modifyPlayerZone")
                        || highestPriorityZone.getPerms().contains(genericPerm);
            } else {
                return highestPriorityZone.getPerms().contains(neededPerm)
                        || highestPriorityZone.getPerms().contains(administratePerm)
                        || this.hasPermission("Zone2.modifyServerZone")
                        || highestPriorityZone.getPerms().contains(genericPerm);
            }

        }
    }
        /*if (inPlayerZone && inServerZone) {
            Zone highestPriority = l.getHighestPriorityZone();
            if (highestPriority instanceof PlayerZone) {
                if (inSubZone) {
                    SubZone highestPrioritySubZone = l.getHighestPrioritySubZone();
                    Permission neededPerm = new Permission(this, t);
                    Permission administratePerm = new Permission(this, PermissionType.MANAGE);
                    return highestPrioritySubZone.getPerms().contains(neededPerm)
                            || highestPrioritySubZone.getPerms().contains(administratePerm)
                            || highestPrioritySubZone.getPlayerUUID().equals(this.getUniqueId())
                            || this.hasPermission("Zone2.modifyPlayerZone") || this.getName().equals("*");
                } else {
                    PlayerZone highestPriorityPlayerZone = (PlayerZone) highestPriority;
                    Permission neededPerm = new Permission(this, t);
                    Permission administratePerm = new Permission(this, PermissionType.MANAGE);
                    return highestPriorityPlayerZone.getPerms().contains(neededPerm)
                            || highestPriorityPlayerZone.getPerms().contains(administratePerm)
                            || highestPriorityPlayerZone.getPlayerUuid().equals(this.getUniqueId())
                            || this.hasPermission("Zone2.modifyPlayerZone") || this.getName().equals("*");
                }
            } else {
                return this.hasPermission("Zone2.modifyServerZone");
            }
        } else if (inPlayerZone) {
            if (inSubZone) {
                SubZone highestPrioritySubZone = l.getHighestPrioritySubZone();
                Permission neededPerm = new Permission(this, t);
                Permission administratePerm = new Permission(this, PermissionType.MANAGE);
                return highestPrioritySubZone.getPerms().contains(neededPerm)
                        || highestPrioritySubZone.getPerms().contains(administratePerm)
                        || highestPrioritySubZone.getPlayerUUID().equals(this.getUniqueId())
                        || this.hasPermission("Zone2.modifyPlayerZone") || this.getName().equals("*");
            } else {
                PlayerZone highestPriorityPlayerZone = l.getHighestPriorityPlayerZone();
                Permission neededPerm = new Permission(this, t);
                Permission administratePerm = new Permission(this, PermissionType.MANAGE);
                return highestPriorityPlayerZone.getPerms().contains(neededPerm)
                        || highestPriorityPlayerZone.getPerms().contains(administratePerm)
                        || highestPriorityPlayerZone.getPlayerUuid().equals(this.getUniqueId())
                        || this.hasPermission("Zone2.modifyPlayerZone") || this.getName().equals("*");
            }
        } else {
            return this.hasPermission("Zone2.modifyServerZone") || this.getName().equals("*");
        }
    }*/

    /**
     * Function to check, wether the player already has a Zone
     * 
     * @return wether the player has a zone
     * @throws IOException if it cant find a file et al
     */
    public boolean hasZone() throws IOException {
        UUID playerUuid = this.getUniqueId();

        ConfigReader reader = ConfigReader.getInstance();
        List<PlayerZone> playerZoneList = reader.getPlayerZoneList();
        List<UUID> zoneUuids = new ArrayList<>();
        playerZoneList.forEach(element -> zoneUuids.add(element.getPlayerUuid()));

        reader.destroy();
        return zoneUuids.contains(playerUuid);
    }

    public void sendXPMessage(String message, boolean important) {
        if (noNotify == null) {
            noNotify = new HashSet<UUID>();
        }
        if (noNotify.contains(this.getUniqueId())) {
            return;
        }
        if (important) {
            noNotify.add(this.getUniqueId());
            Zoneler p = this;

            PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;
            IChatBaseComponent chat = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
            PacketPlayOutChat packet = new PacketPlayOutChat(chat, ChatMessageType.GAME_INFO);
            con.sendPacket(packet);

            new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;
                    IChatBaseComponent chat = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
                    PacketPlayOutChat packet = new PacketPlayOutChat(chat, ChatMessageType.GAME_INFO);
                    con.sendPacket(packet);
                }
            }.runTaskLater(Zone2.getPlugin(), 3);

            new BukkitRunnable() {
                @Override
                public void run() {
                    noNotify.remove(p.getUniqueId());
                }
            }.runTaskLater(Zone2.getPlugin(), 40);
        } else {
            Zoneler p = this;
            PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;
            IChatBaseComponent chat = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
            PacketPlayOutChat packet = new PacketPlayOutChat(chat, ChatMessageType.GAME_INFO);
            con.sendPacket(packet);
        }
    }
}
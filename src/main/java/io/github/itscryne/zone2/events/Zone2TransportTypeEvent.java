package io.github.itscryne.zone2.events;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.extensions.ZLocation;
import io.github.itscryne.zone2.extensions.ZPlayer;
import io.github.itscryne.zone2.perms.PermissionType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Zone2TransportTypeEvent implements Listener {

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) throws IOException {
        if (event.getCause() != TeleportCause.CHORUS_FRUIT) {
            if (event.getCause() != TeleportCause.ENDER_PEARL) {
                return;
            }
        }

        ZLocation eventLocation = new ZLocation(event.getTo());

        if (!eventLocation.inZone()) { // TODO intended?
            return;
        }

        ZPlayer eventPlayer = new ZPlayer(event.getPlayer());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.TRANSPORT);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }

    /*
     * @EventHandler public void onEntityMount (EntityMountEvent event) throws
     * IOException { //Transport or Entity Location eventLocation =
     * event.getMount().getLocation();
     * 
     * if (!(event.getEntity() instanceof Player)) return;
     * 
     * Player eventPlayer = (Player) event.getEntity();
     * 
     * boolean allowed = Zone2PermCheck.isAllowed(eventLocation, eventPlayer,
     * PermissionType.TRANSPORT, Zone2.getPlugin()); event.setCancelled(!allowed);
     * if(!allowed){ eventPlayer.sendXPMessage(ChatColor.RED +
     * Zone2.getPlugin().getConfig().getString("noPermission"), true); }
     */

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws IOException { // Placing boats/minecarts
        List<Material> vehicles = new ArrayList<>(Arrays.asList(Material.ACACIA_BOAT, Material.BIRCH_BOAT,
                Material.SPRUCE_BOAT, Material.OAK_BOAT, Material.SPRUCE_BOAT, Material.JUNGLE_BOAT, Material.MINECART,
                Material.CHEST_MINECART, Material.FURNACE_MINECART, Material.HOPPER_MINECART));

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || !vehicles.contains(event.getMaterial())) {
            return;
        }

        ZPlayer eventPlayer = new ZPlayer(event.getPlayer());
        ZLocation eventLocation = new ZLocation(event.getClickedBlock().getLocation());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.TRANSPORT);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }
}
package io.github.itscryne.zone2.events;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.perms.PermissionType;
import io.github.itscryne.zone2.extensions.Zonecation;
import io.github.itscryne.zone2.extensions.Zoneler;

public class Zone2TransportTypeEvent implements Listener {
    public Zone2TransportTypeEvent(){}

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) throws IOException {
        if (event.getCause() != TeleportCause.CHORUS_FRUIT) {
            if (event.getCause() != TeleportCause.ENDER_PEARL) {
                return;
            }
        }

        Zonecation eventLocation = new Zonecation(event.getTo());

        if (!eventLocation.inZone())
            return;

        Zoneler eventPlayer = new Zoneler(event.getPlayer());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.TRANSPORT);
        Zone2.getPlugin().getLogger().info(String.valueOf(allowed));
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
     * PermissionType.TRANSPORT, Zone2.getPlugin());
     * Zone2.getPlugin().getLogger().info(String.valueOf(allowed)); }
     */

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws IOException { // Placing boats/minecarts
        List<Material> vehicles = new ArrayList<>(Arrays.asList(
           Material.ACACIA_BOAT,
           Material.BIRCH_BOAT,
           Material.SPRUCE_BOAT,
           Material.OAK_BOAT,
           Material.SPRUCE_BOAT,
           Material.JUNGLE_BOAT,
           Material.MINECART,
           Material.CHEST_MINECART,
           Material.FURNACE_MINECART,
           Material.HOPPER_MINECART
        ));

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock().getLocation() == null || !vehicles.contains(event.getClickedBlock().getType())) return;

        Zoneler eventPlayer = new Zoneler(event.getPlayer());
        Zonecation eventLocation = new Zonecation(event.getClickedBlock().getLocation());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.TRANSPORT);
        Zone2.getPlugin().getLogger().info(String.valueOf(allowed));
     }
}
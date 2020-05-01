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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Zone2DoorsTypeEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws IOException {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }
        if (event.getClickedBlock() == null){
            return;
        }

        List<Material> doors = new ArrayList<>( // OAK things allowed TODO: Make ignoring oak disableable in config
                Arrays.asList(Material.DARK_OAK_DOOR, Material.DARK_OAK_FENCE_GATE, Material.DARK_OAK_TRAPDOOR,
                        Material.SPRUCE_DOOR, Material.SPRUCE_FENCE_GATE, Material.SPRUCE_TRAPDOOR, Material.BIRCH_DOOR,
                        Material.BIRCH_FENCE_GATE, Material.BIRCH_TRAPDOOR, Material.JUNGLE_DOOR,
                        Material.JUNGLE_FENCE_GATE, Material.JUNGLE_TRAPDOOR, Material.ACACIA_DOOR,
                        Material.ACACIA_FENCE_GATE, Material.ACACIA_TRAPDOOR));

        if (!(doors.contains(event.getClickedBlock().getType()))) {
            return;
        }

        ZLocation eventLocation = new ZLocation(event.getClickedBlock().getLocation());
        ZPlayer eventPlayer = new ZPlayer(event.getPlayer());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.DOORS);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }
}
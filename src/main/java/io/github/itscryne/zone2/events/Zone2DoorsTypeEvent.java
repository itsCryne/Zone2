package io.github.itscryne.zone2.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.perms.PermissionType;

public class Zone2DoorsTypeEvent implements Listener {
    private Zone2 plugin;
    public Zone2DoorsTypeEvent(Zone2 plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent event) throws IOException {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getClickedBlock() == null) return;

        List<Material> doors = new ArrayList<>( //OAK things allowed TODO: Make ignoring oak disableable in config
            Arrays.asList(
                Material.DARK_OAK_DOOR,
                Material.DARK_OAK_FENCE_GATE,
                Material.DARK_OAK_TRAPDOOR,
                Material.SPRUCE_DOOR,
                Material.SPRUCE_FENCE_GATE,
                Material.SPRUCE_TRAPDOOR,
                Material.BIRCH_DOOR,
                Material.BIRCH_FENCE_GATE,
                Material.BIRCH_TRAPDOOR,
                Material.JUNGLE_DOOR,
                Material.JUNGLE_FENCE_GATE,
                Material.JUNGLE_TRAPDOOR,
                Material.ACACIA_DOOR,
                Material.ACACIA_FENCE_GATE,
                Material.ACACIA_TRAPDOOR
            ));

        if(!(doors.contains(event.getClickedBlock().getType()))) return;

        Location eventLocation = event.getClickedBlock().getLocation();
        Player eventPlayer = event.getPlayer();

        boolean allowed = Zone2PermCheck.isAllowed(eventLocation, eventPlayer, PermissionType.DOORS, this.plugin);
        this.plugin.getLogger().info(String.valueOf(allowed));
    }
}
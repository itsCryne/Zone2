package io.github.itscryne.zone2.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.perms.PermissionType;
import io.github.itscryne.zone2.extensions.Zonecation;
import io.github.itscryne.zone2.extensions.Zoneler;

public class Zone2RedstoneTypeEvent implements Listener {
    public Zone2RedstoneTypeEvent(){}

    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent event) throws IOException {
        List<Material> clickyRedstoneComponents = new ArrayList<>(Arrays.asList( //OAK things allowed TODO: Make disableable in config
            Material.NOTE_BLOCK,
            Material.LEVER,
            Material.STONE_BUTTON,
            Material.SPRUCE_BUTTON,
            Material.BIRCH_BUTTON,
            Material.JUNGLE_BUTTON,
            Material.ACACIA_BUTTON,
            Material.DARK_OAK_BUTTON,
            Material.DAYLIGHT_DETECTOR,
            Material.REPEATER,
            Material.COMPARATOR
        ));

        List<Material> physicalRedstoneComponents = new ArrayList<>(Arrays.asList(
            Material.STONE_PRESSURE_PLATE,
            Material.SPRUCE_PRESSURE_PLATE,
            Material.BIRCH_PRESSURE_PLATE,
            Material.JUNGLE_PRESSURE_PLATE,
            Material.ACACIA_PRESSURE_PLATE,
            Material.DARK_OAK_PRESSURE_PLATE,
            Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Material.HEAVY_WEIGHTED_PRESSURE_PLATE
        ));

        if(event.getClickedBlock() == null) return;
        if(!(event.getAction().equals(Action.PHYSICAL) && physicalRedstoneComponents.contains(event.getClickedBlock().getType()))){
            if(!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && clickyRedstoneComponents.contains(event.getClickedBlock().getType()))){
                return;
            }
        }

        Zonecation eventLocation = (Zonecation) event.getClickedBlock().getLocation();
        Zoneler eventPlayer = (Zoneler) event.getPlayer();

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.REDSTONE);
        Zone2.getPlugin().getLogger().info(String.valueOf(allowed));
    }
}
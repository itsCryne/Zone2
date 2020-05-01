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

public class Zone2RedstoneTypeEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws IOException {
        List<Material> clickyRedstoneComponents = new ArrayList<>(Arrays.asList( // OAK things allowed TODO: Make OAK
                                                                                 // disableable in config
                Material.NOTE_BLOCK, Material.LEVER, Material.STONE_BUTTON, Material.SPRUCE_BUTTON,
                Material.BIRCH_BUTTON, Material.JUNGLE_BUTTON, Material.ACACIA_BUTTON, Material.DARK_OAK_BUTTON,
                Material.DAYLIGHT_DETECTOR, Material.REPEATER, Material.COMPARATOR));

        List<Material> physicalRedstoneComponents = new ArrayList<>(Arrays.asList(Material.STONE_PRESSURE_PLATE,
                Material.SPRUCE_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE,
                Material.ACACIA_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE,
                Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.HEAVY_WEIGHTED_PRESSURE_PLATE));

        if (event.getClickedBlock() == null) {
            return;
        }
        if (!(event.getAction().equals(Action.PHYSICAL)
                && physicalRedstoneComponents.contains(event.getClickedBlock().getType()))) {
            if (!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                    && clickyRedstoneComponents.contains(event.getClickedBlock().getType()))) {
                return;
            }
        }

        ZLocation eventLocation = new ZLocation(event.getClickedBlock().getLocation());
        ZPlayer eventPlayer = new ZPlayer(event.getPlayer());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.REDSTONE);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }
}
package io.github.itscryne.zone2.events;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.extensions.ZLocation;
import io.github.itscryne.zone2.extensions.ZPlayer;
import io.github.itscryne.zone2.perms.PermissionType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Zone2BuildTypeEvent implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) throws IOException {
        ZLocation eventLocation = new ZLocation(event.getBlock().getLocation());
        ZPlayer eventPlayer = new ZPlayer(event.getPlayer());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.BUILD);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) throws IOException {
        ZLocation eventLocation = new ZLocation(event.getBlock().getLocation());
        ZPlayer eventPlayer = new ZPlayer(event.getPlayer());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.BUILD);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws IOException {
        List<Material> hoes = new ArrayList<>(Arrays.asList(Material.WOODEN_HOE, Material.STONE_HOE,
                Material.GOLDEN_HOE, Material.IRON_HOE, Material.DIAMOND_HOE));

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        if (event.getItem() == null) {
            return;
        }
        if (hoes.contains(event.getItem().getType())) {
            return;
        }

        ZLocation eventLocation = new ZLocation(event.getClickedBlock().getLocation());
        ZPlayer eventPlayer = new ZPlayer(event.getPlayer());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.BUILD);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) throws IOException {
        if (event.getRightClicked().getType() != EntityType.ITEM_FRAME) {
            return;
        }

        ZLocation eventLocation = new ZLocation(event.getRightClicked().getLocation());
        ZPlayer eventPlayer = new ZPlayer(event.getPlayer());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.BUILD);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }
}
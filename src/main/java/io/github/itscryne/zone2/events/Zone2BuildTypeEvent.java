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
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.perms.PermissionType;

public class Zone2BuildTypeEvent implements Listener {
    private Zone2 plugin;
    public Zone2BuildTypeEvent(Zone2 plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace (BlockPlaceEvent event) throws IOException {
        Location eventLocation = event.getBlock().getLocation();
        Player eventPlayer = event.getPlayer();

        boolean allowed = Zone2PermCheck.isAllowed(eventLocation, eventPlayer, PermissionType.BUILD, this.plugin);
        this.plugin.getLogger().info(String.valueOf(allowed));
    }

    @EventHandler
    public void onPlayerBucketFill (PlayerBucketFillEvent event) throws IOException {
        Location eventLocation = event.getBlock().getLocation();
        Player eventPlayer = event.getPlayer();

        boolean allowed = Zone2PermCheck.isAllowed(eventLocation, eventPlayer, PermissionType.BUILD, this.plugin);
        this.plugin.getLogger().info(String.valueOf(allowed));
    }

    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent event) throws IOException { //TODO: Just Farmland, sth else?
        List<Material> hoes = new ArrayList<>(Arrays.asList(
            Material.WOODEN_HOE,
            Material.STONE_HOE,
            Material.GOLDEN_HOE,
            Material.IRON_HOE,
            Material.DIAMOND_HOE
        ));

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getClickedBlock() == null) return;
        if(event.getItem() == null) return;
        if(!hoes.contains(event.getItem().getType())) return;

        Location eventLocation = event.getClickedBlock().getLocation();
        Player eventPlayer = event.getPlayer();

        boolean allowed = Zone2PermCheck.isAllowed(eventLocation, eventPlayer, PermissionType.BUILD, this.plugin);
        this.plugin.getLogger().info(String.valueOf(allowed));
    }
}
package io.github.itscryne.zone2.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.perms.PermissionType;
import io.github.itscryne.zone2.extensions.Zonecation;
import io.github.itscryne.zone2.extensions.Zoneler;

public class Zone2BuildTypeEvent implements Listener {
    public Zone2BuildTypeEvent(){}

    @EventHandler
    public void onBlockPlace (BlockPlaceEvent event) throws IOException {
        Zonecation eventLocation = new Zonecation(event.getBlock().getLocation());
        Zoneler eventPlayer = new Zoneler(event.getPlayer());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.BUILD);
        Zone2.getPlugin().getLogger().info(String.valueOf(allowed));
    }

    @EventHandler
    public void onPlayerBucketFill (PlayerBucketFillEvent event) throws IOException {
        Zonecation eventLocation = new Zonecation(event.getBlock().getLocation());
        Zoneler eventPlayer = new Zoneler(event.getPlayer());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.BUILD);
        Zone2.getPlugin().getLogger().info(String.valueOf(allowed));
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

        Zonecation eventLocation = new Zonecation(event.getClickedBlock().getLocation());
        Zoneler eventPlayer = new Zoneler(event.getPlayer());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.BUILD);
        Zone2.getPlugin().getLogger().info(String.valueOf(allowed));
    }
}
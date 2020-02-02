package io.github.itscryne.zone2.events;

import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.extensions.Zonecation;
import io.github.itscryne.zone2.extensions.Zoneler;
import io.github.itscryne.zone2.perms.PermissionType;

public class Zone2DestroyTypeEvent implements Listener {
    public Zone2DestroyTypeEvent(){}

    @EventHandler
    public void onBlockBreak (BlockBreakEvent event) throws IOException {
        Zonecation eventLocation = new Zonecation(event.getBlock().getLocation());
        Zoneler eventPlayer = new Zoneler(event.getPlayer());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.DESTROY);
        Zone2.getPlugin().getLogger().info(String.valueOf(allowed));
    }

    @EventHandler
    public void onHangingBreakByEntity (HangingBreakByEntityEvent event) throws IOException {
        Zonecation eventLocation = new Zonecation(event.getEntity().getLocation());
        if (!(event.getRemover() instanceof Player)){
            return;
        }

        Zoneler eventPlayer = new Zoneler((Player)event.getRemover());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.DESTROY);
        Zone2.getPlugin().getLogger().info(String.valueOf(allowed));
    }

    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent event) throws IOException { //TODO: Only Farmland so far, are there other things?
        if (event.getClickedBlock() == null) return;

        Zonecation eventLocation = new Zonecation(event.getClickedBlock().getLocation());
        Zoneler eventPlayer = new Zoneler(event.getPlayer());
        Action action = event.getAction();

        if (!(action.equals(Action.PHYSICAL) && eventLocation.getBlock().getType().equals(Material.FARMLAND))){
            return;
        }

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.DESTROY);
        Zone2.getPlugin().getLogger().info(String.valueOf(allowed));
    }

    @EventHandler
    public void onPlayerBucketFill (PlayerBucketFillEvent event) throws IOException {
        Zonecation eventLocation = new Zonecation(event.getBlock().getLocation());
        Zoneler eventPlayer = new Zoneler(event.getPlayer());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.DESTROY);
        Zone2.getPlugin().getLogger().info(String.valueOf(allowed));
    }
}
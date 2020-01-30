package io.github.itscryne.zone2.events;

import java.io.IOException;

import org.bukkit.Location;
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
import io.github.itscryne.zone2.perms.PermissionType;

public class Zone2DestroyTypeEvent implements Listener {
    private Zone2 plugin;
    public Zone2DestroyTypeEvent(Zone2 plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak (BlockBreakEvent event) throws IOException {
        Location eventLocation = event.getBlock().getLocation();
        Player eventPlayer = event.getPlayer();

        boolean allowed = Zone2PermCheck.isAllowed(eventLocation, eventPlayer, PermissionType.DESTROY, this.plugin);
        this.plugin.getLogger().info(String.valueOf(allowed));
    }

    @EventHandler
    public void onHangingBreakByEntity (HangingBreakByEntityEvent event) throws IOException {
        Location eventLocation = event.getEntity().getLocation();
        if (!(event.getRemover() instanceof Player)){
            return;
        }

        Player eventPlayer = (Player) event.getRemover();

        boolean allowed = Zone2PermCheck.isAllowed(eventLocation, eventPlayer, PermissionType.DESTROY, this.plugin);
        this.plugin.getLogger().info(String.valueOf(allowed));
    }

    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent event) throws IOException { //TODO: Only Farmland so far, are there other things?
        if (event.getClickedBlock() == null) return;

        Location eventLocation = event.getClickedBlock().getLocation();
        Player eventPlayer = event.getPlayer();
        Action action = event.getAction();

        if (!(action.equals(Action.PHYSICAL) && eventLocation.getBlock().getType().equals(Material.FARMLAND))){
            return;
        }

        boolean allowed = Zone2PermCheck.isAllowed(eventLocation, eventPlayer, PermissionType.DESTROY, this.plugin);
        this.plugin.getLogger().info(String.valueOf(allowed));
    }

    @EventHandler
    public void onPlayerBucketFill (PlayerBucketFillEvent event) throws IOException {
        Location eventLocation = event.getBlock().getLocation();
        Player eventPlayer = event.getPlayer();

        boolean allowed = Zone2PermCheck.isAllowed(eventLocation, eventPlayer, PermissionType.DESTROY, this.plugin);
        this.plugin.getLogger().info(String.valueOf(allowed));
    }
}
package io.github.itscryne.zone2.events;

import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

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

        boolean allowed = Zone2PermCheck.isAllowed(eventLocation, eventPlayer, PermissionType.BREAK, this.plugin);
        this.plugin.getLogger().info(String.valueOf(allowed));
    }
}
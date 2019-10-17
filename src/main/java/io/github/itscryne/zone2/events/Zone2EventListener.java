package io.github.itscryne.zone2.events;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.perms.PermissionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.IOException;

public class Zone2EventListener implements Listener {
    private Zone2 plugin;
    public Zone2EventListener(Zone2 plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak (BlockBreakEvent event) throws IOException {
        boolean allowed = Zone2PermCheck.isAllowed(event.getBlock().getLocation(), event.getPlayer(), PermissionType.BREAK, this.plugin);
        this.plugin.getLogger().info(String.valueOf(allowed));
    }
}

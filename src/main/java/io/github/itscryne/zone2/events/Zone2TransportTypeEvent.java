package io.github.itscryne.zone2.events;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.perms.PermissionType;

public class Zone2TransportTypeEvent implements Listener {
    private Zone2 plugin;

    public Zone2TransportTypeEvent(Zone2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) throws IOException { //TODO: Teleport outside of zones allow into config
        Location eventLocation = event.getTo();

        if (!Zone2PermCheck.inZone(eventLocation, this.plugin)) return;

        Player eventPlayer = event.getPlayer();

        boolean allowed = Zone2PermCheck.isAllowed(eventLocation, eventPlayer, PermissionType.TRANSPORT, this.plugin);
        this.plugin.getLogger().info(String.valueOf(allowed));
     }

     @EventHandler
     public void onEntityMount (EntityMountEvent event) throws IOException {
         Location eventLocation = event.getEntity().getLocation();

         if (!(event.getMount() instanceof Player)) return;

         Player eventPlayer = (Player) event.getMount();

         boolean allowed = Zone2PermCheck.isAllowed(eventLocation, eventPlayer, PermissionType.TRANSPORT, this.plugin);
         this.plugin.getLogger().info(String.valueOf(allowed));
     }
}
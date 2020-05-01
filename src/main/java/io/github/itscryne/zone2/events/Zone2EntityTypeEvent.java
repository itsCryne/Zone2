package io.github.itscryne.zone2.events;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.extensions.ZLocation;
import io.github.itscryne.zone2.extensions.ZPlayer;
import io.github.itscryne.zone2.perms.PermissionType;
import org.bukkit.ChatColor;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

import java.io.IOException;

public class Zone2EntityTypeEvent implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) throws IOException {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (event.getEntity() instanceof ItemFrame) {
            return;
        }

        ZPlayer eventPlayer = new ZPlayer((Player) event.getDamager());
        ZLocation eventLocation = new ZLocation(event.getEntity().getLocation());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.ENTITY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }

    @EventHandler
    public void onPlayerShearEntity(PlayerShearEntityEvent event) throws IOException {
        ZPlayer eventPlayer = new ZPlayer(event.getPlayer());
        ZLocation eventLocation = new ZLocation(event.getEntity().getLocation());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.ENTITY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }
}
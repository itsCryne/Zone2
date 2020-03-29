package io.github.itscryne.zone2.events;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

import io.github.itscryne.zone2.extensions.Zonecation;
import io.github.itscryne.zone2.extensions.Zoneler;
import io.github.itscryne.zone2.perms.PermissionType;

public class Zone2EntityTypeEvent implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) throws IOException {
        if (!(event.getDamager() instanceof Player)){
            return;
        }
        if (event.getEntity() instanceof ItemFrame){
            return;
        }

        Zoneler eventPlayer = new Zoneler((Player) event.getDamager());
        Zonecation eventLocation = new Zonecation(event.getEntity().getLocation());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.ENTITY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + "Das darfst du hier nicht", true);
        }
    }

    @EventHandler
    public void onPlayerShearEntity(PlayerShearEntityEvent event) throws IOException {
        Zoneler eventPlayer = new Zoneler((Player) event.getPlayer());
        Zonecation eventLocation = new Zonecation(event.getEntity().getLocation());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.ENTITY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + "Das darfst du hier nicht", true);
        }
    }
}
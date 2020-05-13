package io.github.itscryne.zone2.events;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.extensions.ZLocation;
import io.github.itscryne.zone2.extensions.ZPlayer;
import io.github.itscryne.zone2.perms.PermissionType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Zone2DestroyTypeEvent implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) throws IOException {
        ZLocation eventLocation = new ZLocation(event.getBlock().getLocation());
        ZPlayer eventPlayer = new ZPlayer(event.getPlayer());

        List<Material> inventories = new ArrayList<>(Arrays.asList(Material.CHEST, Material.FURNACE, Material.JUKEBOX,
                Material.SHULKER_BOX, Material.BARREL, Material.SMOKER, Material.BLAST_FURNACE, Material.CAMPFIRE));

        boolean allowed;
        if (inventories.contains(event.getBlock().getType())) {
            allowed = eventPlayer.isAllowed(eventLocation, PermissionType.DESTROY)
                    && eventPlayer.isAllowed(eventLocation, PermissionType.INVENTORY);
        } else {
            allowed = eventPlayer.isAllowed(eventLocation, PermissionType.DESTROY);
        }
        event.setCancelled(!allowed);

        List<Material> specialNoBreakingMaterials = new ArrayList<>(Arrays.asList(
                Material.STONE_SWORD,
                Material.DIAMOND_SWORD,
                Material.GOLDEN_SWORD,
                Material.IRON_SWORD,
                Material.WOODEN_SWORD,
                Material.DEBUG_STICK
        ));

        if (specialNoBreakingMaterials.contains(event.getPlayer().getInventory().getItemInMainHand().getType())) {
            event.setCancelled(true);
        }
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) throws IOException {
        ZLocation eventLocation = new ZLocation(event.getEntity().getLocation());
        if (event.getRemover() instanceof Projectile) {
            if (((Projectile) event.getRemover()).getShooter() instanceof Player) {
                ZPlayer eventPlayer = new ZPlayer((Player) Objects.requireNonNull(((Projectile) event.getRemover()).getShooter()));
                boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.DESTROY);

                event.setCancelled(!allowed);
                if (!allowed) {
                    eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
                }
                return;
            }
        }

        if (!(event.getRemover() instanceof Player)) {
            return;
        }

        ZPlayer eventPlayer = new ZPlayer((Player) event.getRemover());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.DESTROY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws IOException {
        if (event.getClickedBlock() == null) {
            return;
        }

        ZLocation eventLocation = new ZLocation(event.getClickedBlock().getLocation());
        ZPlayer eventPlayer = new ZPlayer(event.getPlayer());
        Action action = event.getAction();

        if (!(action.equals(Action.PHYSICAL) && eventLocation.getBlock().getType().equals(Material.FARMLAND))) {
            return;
        }

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.DESTROY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) throws IOException {
        ZLocation eventLocation = new ZLocation(event.getBlock().getLocation());
        ZPlayer eventPlayer = new ZPlayer(event.getPlayer());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.DESTROY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) throws IOException {
        if (!(event.getEntity() instanceof ArmorStand)) {
            return;
        }
        if (!(event.getDamager() instanceof Player || event.getDamager() instanceof Projectile)) {
            return;
        }

        if (event.getDamager() instanceof Player) {
            ZLocation eventLocation = new ZLocation(event.getEntity().getLocation());
            ZPlayer eventPlayer = new ZPlayer((Player) event.getDamager());

            boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.DESTROY);
            event.setCancelled(!allowed);
            if (!allowed) {
                eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"),
                        true);
            }
        }

        if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
            ZLocation eventLocation = new ZLocation(event.getEntity().getLocation());
            ZPlayer eventPlayer = new ZPlayer(((Player) ((Projectile) event.getDamager()).getShooter()));

            boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.DESTROY);
            event.setCancelled(!allowed);
            if (!allowed) {
                eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"),
                        true);
            }
        }

    }
}
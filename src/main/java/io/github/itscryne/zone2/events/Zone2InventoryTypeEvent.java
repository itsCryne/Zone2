package io.github.itscryne.zone2.events;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.extensions.ZLocation;
import io.github.itscryne.zone2.extensions.ZPlayer;
import io.github.itscryne.zone2.perms.PermissionType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Zone2InventoryTypeEvent implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) throws IOException {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        List<InventoryType> allowedInvs = new ArrayList<>(Arrays.asList(InventoryType.ENCHANTING, InventoryType.LOOM,
                InventoryType.CARTOGRAPHY, InventoryType.ENDER_CHEST, InventoryType.STONECUTTER,
                InventoryType.WORKBENCH, InventoryType.GRINDSTONE, InventoryType.CRAFTING, InventoryType.CREATIVE,
                InventoryType.PLAYER));

        ZPlayer eventPlayer = new ZPlayer((Player) event.getPlayer());
        ZLocation eventLocation;

        if (event.getInventory().getLocation() == null) {
            eventLocation = new ZLocation(event.getPlayer().getLocation());
        } else {
            eventLocation = new ZLocation(event.getInventory().getLocation());
        }

        if (allowedInvs.contains(event.getInventory().getType())) {
            event.setCancelled(false);
            return;
        }
        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.INVENTORY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws IOException {
        List<Material> inventoryMaterials = new ArrayList<>(Arrays.asList(Material.FLOWER_POT, Material.CAMPFIRE,
                Material.CAULDRON, Material.COMPOSTER, Material.ITEM_FRAME));

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null
                || !inventoryMaterials.contains(event.getClickedBlock().getType())) {
            if (event.getClickedBlock() == null
                    || !(event.getClickedBlock().getType().toString().startsWith("POTTED_"))) { // KTSD
                return;
            }
        }

        ZPlayer eventPlayer = new ZPlayer(event.getPlayer());
        ZLocation eventLocation = new ZLocation(event.getClickedBlock().getLocation());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.INVENTORY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) throws IOException {
        ZPlayer eventPlayer = new ZPlayer(event.getPlayer());
        ZLocation eventLocation = new ZLocation(event.getRightClicked().getLocation());
        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.INVENTORY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) throws IOException {
        if (!(event.getEntity() instanceof ItemFrame)){
            return;
        }

        ZPlayer eventPlayer;
        if (event.getDamager() instanceof Player){
            eventPlayer = new ZPlayer((Player) event.getDamager());
        } else if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player){
            eventPlayer = new ZPlayer((Player) ((Projectile) event.getDamager()).getShooter());
        } else {
            return;
        }
        ZLocation eventLocation = new ZLocation(event.getEntity().getLocation());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.INVENTORY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noPermission"), true);
        }
    }
}
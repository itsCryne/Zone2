package io.github.itscryne.zone2.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import io.github.itscryne.zone2.extensions.Zonecation;
import io.github.itscryne.zone2.extensions.Zoneler;
import io.github.itscryne.zone2.perms.PermissionType;

public class Zone2InventoryTypeEvent implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) throws IOException {
        if (!(event.getPlayer() instanceof Player)){
            return;
        }

        List<InventoryType> allowedInvs = new ArrayList<>(Arrays.asList(InventoryType.ENCHANTING, InventoryType.LOOM,
                InventoryType.CARTOGRAPHY, InventoryType.ENDER_CHEST, InventoryType.STONECUTTER,
                InventoryType.WORKBENCH, InventoryType.GRINDSTONE, InventoryType.CRAFTING, InventoryType.CREATIVE,
                InventoryType.PLAYER));

        Zoneler eventPlayer = new Zoneler((Player) event.getPlayer());
        Zonecation eventLocation;

        if (event.getInventory().getLocation() == null) {
            eventLocation = new Zonecation(eventPlayer.getLocation());
        } else {
            eventLocation = new Zonecation(event.getInventory().getLocation());
        }

        if (allowedInvs.contains(event.getInventory().getType())){
            return;
        }
        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.INVENTORY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + "Das darfst du hier nicht", true);
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

        Zoneler eventPlayer = new Zoneler(event.getPlayer());
        Zonecation eventLocation = new Zonecation(event.getClickedBlock().getLocation());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.INVENTORY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + "Das darfst du hier nicht", true);
        }
    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) throws IOException {
        Zoneler eventPlayer = new Zoneler(event.getPlayer());
        Zonecation eventLocation = new Zonecation(event.getRightClicked().getLocation());
        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.INVENTORY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + "Das darfst du hier nicht", true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) throws IOException {
        if (!(event.getEntity() instanceof ItemFrame)){
            return;
        }

        Zoneler eventPlayer = new Zoneler((Player) event.getDamager());
        Zonecation eventLocation = new Zonecation(event.getEntity().getLocation());

        boolean allowed = eventPlayer.isAllowed(eventLocation, PermissionType.INVENTORY);
        event.setCancelled(!allowed);
        if (!allowed) {
            eventPlayer.sendXPMessage(ChatColor.RED + "Das darfst du hier nicht", true);
        }
    }
}
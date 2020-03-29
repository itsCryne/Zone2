package io.github.itscryne.zone2.commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.extensions.Zonecation;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.perms.PermissionType;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.SubZone;

public class Zone2InfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1){
            return false;
        }

        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Bitte gib als ID eine Ganzzahl an");
            return true;
        }

        ConfigReader reader;
        try {
            reader = ConfigReader.getInstance();
        } catch (IOException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            return true;
        }

        List<PlayerZone> playerZoneList;
        try {
            playerZoneList = reader.getPlayerZoneList();
        } catch (FileNotFoundException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            return true;
        }

        PlayerZone selectedZone = playerZoneList.stream().filter(zone -> id == zone.getId()).findFirst().orElse(null);

        if (selectedZone == null) {
            sender.sendMessage(ChatColor.YELLOW + "Zone nicht gefunden");
            return true;
        }

        if (sender instanceof Player
                && !(selectedZone.getPerms().contains(new Permission((Player) sender, PermissionType.MANAGE))
                        || selectedZone.getPlayerUuid().equals(((Player) sender).getUniqueId())
                        || ((Player) sender).hasPermission("Zone2.modifyPlayerZone"))) {
            sender.sendMessage(ChatColor.YELLOW + "Du hast nicht genügend Rechte");
            return true;
        }

        String owner = Bukkit.getOfflinePlayer(selectedZone.getPlayerUuid()).getName();
        Zonecation l1 = selectedZone.getL1();
        Zonecation l2 = selectedZone.getL2();
        List<Permission> permList = selectedZone.getPerms();
        List<SubZone> subZones;

        try {
            subZones = selectedZone.getSubZones();
        } catch (IOException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            return true;
        }

        String message;

        message = ChatColor.DARK_GREEN + "Zone: " + Integer.toString(id) + "\n";
        message = message.concat(ChatColor.WHITE + "Besitzer: " + ChatColor.GOLD + owner + "\n");
        message = message.concat(ChatColor.WHITE + "Ecke 1: " + ChatColor.GOLD + "X: " + l1.getBlockX() + " Z: "
                + l1.getBlockZ() + "\n");
        message = message.concat(ChatColor.WHITE + "Ecke 2: " + ChatColor.GOLD + "X: " + l2.getBlockX() + " Z: "
                + l2.getBlockZ() + "\n");

        message = message.concat(ChatColor.WHITE + "Permissions:\n");

        for (PermissionType t : PermissionType.values()) {
            List<String> permPlayers = new ArrayList<>();
            for (Permission p : permList) {
                if (p.getPerm().equals(t)) {
                    if (p.getP().getName() == null) {
                        permPlayers.add(ChatColor.RED + "  -" + "???" + "\n");
                    } else {
                        permPlayers.add(ChatColor.BLUE + "  -" + p.getP().getName() + "\n");
                    }

                }
            }
            if (!permPlayers.isEmpty()) {
                message = message.concat(ChatColor.GOLD + " -" + t.toString() + ":\n");
                for (String s : permPlayers) {
                    message = message.concat(s);
                }
                message = message.concat("\n");
            }
        }

        message = message.concat(ChatColor.WHITE + "Subzonen:\n");

        for (SubZone zone : subZones) {
            Zonecation subL1 = zone.getL1();
            Zonecation subL2 = zone.getL2();
            List<Permission> subPermList = zone.getPerms();

            message = message.concat(ChatColor.GOLD + " -Subzone " + zone.getId() + ":\n");

            message = message.concat(ChatColor.BLUE + "  -Ecke 1: " + ChatColor.GREEN + "X: " + subL1.getBlockX()
                    + " Z: " + subL1.getBlockZ() + "\n");
            message = message.concat(ChatColor.BLUE + "  -Ecke 2: " + ChatColor.GREEN + "X: " + subL2.getBlockX()
                    + " Z: " + subL2.getBlockZ() + "\n");

            message = message.concat(ChatColor.BLUE + "  -Permissions:\n");

            for (PermissionType t : PermissionType.values()) {
                List<String> permPlayers = new ArrayList<>();
                for (Permission p : subPermList) {
                    if (p.getPerm().equals(t)) {
                        if (p.getP().getName() == null) {
                            permPlayers.add(ChatColor.RED + "    -" + "???" + "\n");
                        } else {
                            permPlayers.add(ChatColor.GRAY + "    -" + p.getP().getName() + "\n");
                        }
                    }
                }
                if (!permPlayers.isEmpty()) {
                    message = message.concat(ChatColor.GREEN + "   -" + t.toString() + ":\n");
                    for (String s : permPlayers) {
                        message = message.concat(s);
                    }
                    message = message.concat("\n");
                }
            }

        }

        sender.sendMessage(message);
        return true;
    }
}
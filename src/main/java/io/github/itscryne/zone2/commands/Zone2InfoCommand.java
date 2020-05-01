package io.github.itscryne.zone2.commands;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.extensions.ZLocation;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.perms.PermissionType;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.SubZone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
            sender.sendMessage(Zone2.getPlugin().getConfig().getString("intID"));
            return true;
        }

        ConfigReader reader;
        try {
            reader = ConfigReader.getInstance();
        } catch (IOException e) {
            sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            return true;
        }

        List<PlayerZone> playerZoneList;
        try {
            playerZoneList = reader.getPlayerZoneList();
        } catch (IOException e) {
            sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            ConfigReader.destroy();
            return true;
        }

        PlayerZone selectedZone = playerZoneList.stream().filter(zone -> id == zone.getId()).findFirst().orElse(null);

        if (selectedZone == null) {
            sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("zoneNotFound"));
            return true;
        }

        if (sender instanceof Player
                && !(selectedZone.getPerms().contains(new Permission((Player) sender, PermissionType.MANAGE))
                || selectedZone.getPlayerUuid().equals(((Player) sender).getUniqueId())
                || sender.hasPermission("zone2.modifyPlayerZone"))) {
            sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("noPerm"));
            return true;
        }

        String owner = Bukkit.getOfflinePlayer(selectedZone.getPlayerUuid()).getName();
        ZLocation l1 = selectedZone.getL1();
        ZLocation l2 = selectedZone.getL2();
        List<Permission> permList = selectedZone.getPerms();
        List<SubZone> subZones;

        try {
            subZones = selectedZone.getSubZones();
        } catch (IOException e) {
            sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            return true;
        }

        String message;

        message = ChatColor.DARK_GREEN + Zone2.getPlugin().getConfig().getString("zoneWord") + id + "\n";
        message = message.concat(ChatColor.WHITE + Zone2.getPlugin().getConfig().getString("ownerWord") + ChatColor.GOLD + owner + "\n");
        message = message.concat(ChatColor.WHITE + Zone2.getPlugin().getConfig().getString("corner1Word") + ChatColor.GOLD + "X: " + l1.getBlockX() + " Z: "
                + l1.getBlockZ() + "\n");
        message = message.concat(ChatColor.WHITE + Zone2.getPlugin().getConfig().getString("corner1Word") + ChatColor.GOLD + "X: " + l2.getBlockX() + " Z: "
                + l2.getBlockZ() + "\n");

        message = message.concat(ChatColor.WHITE + Zone2.getPlugin().getConfig().getString("permissionsWord") + "\n");

        for (PermissionType t : PermissionType.values()) {
            List<String> permPlayers = new ArrayList<>();
            for (Permission p : permList) {
                if (p.getPerm().equals(t)) {
                    if (p.getP().getName() == null) {
                        permPlayers.add(ChatColor.RED + "  -" + Zone2.getPlugin().getConfig().getString("unknownPlayer") + "\n");
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

        message = message.concat(ChatColor.WHITE + Zone2.getPlugin().getConfig().getString("subzonesWord") + "\n");

        for (SubZone zone : subZones) {
            ZLocation subL1 = zone.getL1();
            ZLocation subL2 = zone.getL2();
            List<Permission> subPermList = zone.getPerms();

            message = message.concat(ChatColor.GOLD + " -" + Zone2.getPlugin().getConfig().getString("subzoneWord") + zone.getId() + ":\n");

            message = message.concat(ChatColor.BLUE + "  -" + Zone2.getPlugin().getConfig().getString("corner1Word") + ChatColor.GREEN + "X: " + subL1.getBlockX()
                    + " Z: " + subL1.getBlockZ() + "\n");
            message = message.concat(ChatColor.BLUE + "  -" + Zone2.getPlugin().getConfig().getString("corner2Word") + ChatColor.GREEN + "X: " + subL2.getBlockX()
                    + " Z: " + subL2.getBlockZ() + "\n");

            message = message.concat(ChatColor.BLUE + "  -" + Zone2.getPlugin().getConfig().getString("permissionsWord") + "\n");

            for (PermissionType t : PermissionType.values()) {
                List<String> permPlayers = new ArrayList<>();
                for (Permission p : subPermList) {
                    if (p.getPerm().equals(t)) {
                        if (p.getP().getName() == null) {
                            permPlayers.add(ChatColor.RED + "    -" + Zone2.getPlugin().getConfig().getString("unknownPlayer") + "\n");
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
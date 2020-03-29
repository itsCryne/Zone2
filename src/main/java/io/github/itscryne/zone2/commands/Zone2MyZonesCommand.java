package io.github.itscryne.zone2.commands;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.ServerZone;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Zone2MyZonesCommand implements CommandExecutor { //serverzones
    /**
     * Executes the given command, returning its success. <br>
     * If false is returned, then the "usage" plugin.yml entry for this command (if
     * defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.YELLOW + "Nur Spieler können ihre Zonen abrufen");
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

        if(args.length != 1 || !args[0].equalsIgnoreCase("server") || !((Player)sender).isOp()){
            List<PlayerZone> playerZoneList = null;

            try {
                playerZoneList = reader.getPlayerZoneList();
            } catch (FileNotFoundException e) {
                sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
                Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            }

            if (playerZoneList == null) {
                playerZoneList = new ArrayList<>();
            }

            List<PlayerZone> sendersZones = new ArrayList<>();

            for (PlayerZone i : playerZoneList) {
                if (i.getPlayerUuid().equals(((Player) sender).getUniqueId())) {
                    sendersZones.add(i);
                    continue;
                }
                for (Permission s : i.getPerms()) { //TODO: WHHHHHHHHHHHHHHHHHHHHHHHHAAAAAAAAT TF does this?
                    if (s.getPlayerUuid().equals(((Player) sender).getUniqueId())) {
                        sendersZones.add(i);
                    }
                }
            }

            if (sendersZones.isEmpty()) {
                sender.sendMessage(ChatColor.YELLOW + "Du besitzt keine Zonen");
                return true;
            }

            sender.sendMessage(ChatColor.GREEN + "Du kannst folgende Zonen bearbeiten:");
            for (PlayerZone i : sendersZones) {
                sender.sendMessage("   ID: " + ChatColor.GOLD + i.getId());
            }
        } else {
            List<ServerZone> serverZoneList = null;

            try {
                serverZoneList = reader.getServerZoneList();
            } catch (FileNotFoundException e) {
                sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
                Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            }

            if (serverZoneList == null) {
                serverZoneList = new ArrayList<>();
            }

            List<ServerZone> sendersZones = new ArrayList<>();

            for (ServerZone i : serverZoneList) {
                    sendersZones.add(i);
            }

            if (sendersZones.isEmpty()) {
                sender.sendMessage(ChatColor.YELLOW + "Es gibt keine Serverzonen");
                return true;
            }

            sender.sendMessage(ChatColor.GREEN + "Es gibt die folgenden Serverzonen:");
            for (ServerZone i : sendersZones) {
                sender.sendMessage("   ID: " + ChatColor.GOLD + i.getId() + ChatColor.WHITE + "   Name: " + ChatColor.GOLD + i.getName());
            }
        }
        return true;
    }
}

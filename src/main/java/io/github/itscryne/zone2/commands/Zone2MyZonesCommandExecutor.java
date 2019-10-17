package io.github.itscryne.zone2.commands;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.config.ConfigWriter;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.spaces.PlayerZone;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zone2MyZonesCommandExecutor implements CommandExecutor {
    private Zone2 plugin;
    public Zone2MyZonesCommandExecutor(Zone2 plugin){
        this.plugin = plugin;
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(ChatColor.YELLOW + "Nur Spieler können ihre Zonen abrufen");
            return true;
        }

        ConfigReader reader;

        try {
            reader = ConfigReader.getInstance(this.plugin);
        } catch (IOException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
            e.printStackTrace();
            return true;
        }

        List<PlayerZone> playerZoneList = null;

        try {
            playerZoneList = reader.getPlayerZoneList();
        } catch (FileNotFoundException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
            e.printStackTrace();
        }

        if (playerZoneList == null){
            playerZoneList = new ArrayList<>();
        }

        List<PlayerZone> sendersZones = new ArrayList<>();

        for (PlayerZone i: playerZoneList){
            if(i.getPlayerUuid().equals(((Player) sender).getUniqueId())){
                sendersZones.add(i);
                continue;
            }
            for (Permission s: i.getPerms()){
                if(s.getPlayerUuid().equals(((Player) sender).getUniqueId())){
                    sendersZones.add(i);
                }
            }
        }

        if(sendersZones.isEmpty()){
            //TODO
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + "Du kannst folgende Zonen bearbeiten:");
        for (PlayerZone i: sendersZones){
            sender.sendMessage("   ID: " + ChatColor.GOLD + i.getId());
        }
        return true;
    }
}

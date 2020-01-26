package io.github.itscryne.zone2.commands;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.config.ConfigWriter;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.perms.PermissionType;
import io.github.itscryne.zone2.spaces.PlayerZone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Zone2PermissionCommand implements CommandExecutor {
    Zone2 plugin;

    public Zone2PermissionCommand(Zone2 plugin){
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { //add|remove who what id
        if (args.length != 4) {
            return false;
        }

        if (!args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("delete")){
            return false;
        }

        Player who = Bukkit.getPlayer(args[1]);
        if (who == null){
            sender.sendMessage(ChatColor.YELLOW + "Dieser Spieler existiert nicht");
            return true;
        }

        List<String> enumValues = new ArrayList<>(Arrays.asList(Arrays.stream(PermissionType.class.getEnumConstants()).map(Enum::name).toArray(String[]::new)));
        boolean validValue = enumValues.stream().anyMatch(args[2]::equalsIgnoreCase);
        if (!validValue){
            sender.sendMessage(ChatColor.YELLOW + "Diese Permission existiert nicht!");
            sender.sendMessage(ChatColor.DARK_GREEN + "Mögliche Permissions sind:");
            for (String i : enumValues){
                sender.sendMessage(ChatColor.GREEN + "  - " + i);
            }
            return true;
        }

        int id;
        try{
            id = Integer.parseInt(args[3]);
        } catch (NumberFormatException e){
            sender.sendMessage("Bitte gib als ID eine Ganzzahl an");
            return true;
        }

        if (args[0].equalsIgnoreCase("add")){
            PermissionType permType = PermissionType.valueOf(args[2].toUpperCase());
            Permission perm = new Permission(who, permType);

            ConfigReader reader;
            ConfigWriter writer;
            try{
                reader = ConfigReader.getInstance(this.plugin);
                writer = ConfigWriter.getInstance(this.plugin);
            } catch (IOException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
                return true;
            }

            List<PlayerZone> playerZoneList = new ArrayList<>();
            try {
                playerZoneList = reader.getPlayerZoneList();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
                return true;
            }

            if (playerZoneList == null){
                sender.sendMessage(ChatColor.YELLOW + "Zone nicht gefunden");
                return true;
            }

            PlayerZone zone = null;

            for (PlayerZone i : playerZoneList){
                if (id == i.getId()){
                    zone = i;
                }
            }

            if (zone == null){
                sender.sendMessage(ChatColor.YELLOW + "Zone nicht gefunden");
                return true;
            }

            Permission zoneAdmin = new Permission((Player) sender, PermissionType.ADMINISTRATE);

            if (!zone.getPlayerUuid().equals(((Player) sender).getUniqueId())) {
                if (!zone.getPerms().contains(zoneAdmin)) {
                    if (!sender.hasPermission("Zone2.modifyPermissions"))
                    sender.sendMessage(ChatColor.YELLOW + "Du hast hierzu keine Berechtigung");
                    return true;
                }
            }

            List<Permission> perms = new ArrayList<>(zone.getPerms());

            for (Permission i: perms){
                if (i.getP().equals(perm.getP())){
                    if (i.getPerm().equals(perm.getPerm())){
                        sender.sendMessage(ChatColor.YELLOW + "Du hast dem Spieler " + ChatColor.RESET + who.getDisplayName() + ChatColor.YELLOW + " das Recht " + ChatColor.RED + args[2].toUpperCase() + ChatColor.YELLOW + " schon gewährt");
                        return true;
                    }
                }
            }

            perms.add(perm);
            zone.setPerms(perms);

            try {
                writer = ConfigWriter.getInstance(this.plugin);
            } catch (IOException e) {
                sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
                e.printStackTrace();
                return true;
            }

            try {
                writer.deletePlayerZone(zone.getId());
                writer.writePlayerZone(zone);
                String message = ChatColor.GREEN + "Dem Spieler " + ChatColor.RESET + who.getDisplayName() + ChatColor.GREEN +
                        " wurde das Recht " + ChatColor.DARK_GREEN + args[2].toUpperCase() + ChatColor.GREEN + " gewährt";
                sender.sendMessage(message);
                return true;
            } catch (IOException e) {
                sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
                e.printStackTrace();
                return true;
            }

        } else if (args[0].equalsIgnoreCase("delete")){
            PermissionType permType = PermissionType.valueOf(args[2].toUpperCase());

            ConfigReader reader;
            ConfigWriter writer;
            try{
                reader = ConfigReader.getInstance(this.plugin);
                writer = ConfigWriter.getInstance(this.plugin);
            } catch (IOException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
                return true;
            }

            List<PlayerZone> playerZoneList = new ArrayList<>();
            try {
                playerZoneList = reader.getPlayerZoneList();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
                return true;
            }

            if (playerZoneList == null){
                sender.sendMessage(ChatColor.YELLOW + "Zone nicht gefunden");
                return true;
            }

            PlayerZone zone = null;

            for (PlayerZone i : playerZoneList){
                if (id == i.getId()){
                    zone = i;
                }
            }

            if (zone == null){
                sender.sendMessage(ChatColor.YELLOW + "Zone nicht gefunden");
                return true;
            }

            Permission zoneAdmin = new Permission((Player) sender, PermissionType.ADMINISTRATE);

            if (!zone.getPlayerUuid().equals(((Player) sender).getUniqueId())) {
                if (!zone.getPerms().contains(zoneAdmin)) {
                    if (!sender.hasPermission("Zone2.modifyPermissions"))
                        sender.sendMessage(ChatColor.YELLOW + "Du hast hierzu keine Berechtigung");
                    return true;
                }
            }

            List<Permission> perms = new ArrayList<>(zone.getPerms());
            boolean removed = false;
            Permission toRemove = null;

            for (Permission i: perms){
                if (i.getP().equals(who)){
                    if (i.getPerm().equals(permType)){
                        toRemove = i;
                        removed = true;
                    }
                }
            }

            if (removed){
                perms.remove(toRemove);
                zone.setPerms(perms);
            }

            if (!removed){
                sender.sendMessage(ChatColor.YELLOW + "Der Spieler " + ChatColor.RESET + who.getDisplayName() + ChatColor.YELLOW + " besitzt die Berechtigung " + ChatColor.RED + args[2].toUpperCase() + ChatColor.YELLOW + " nicht");
                return true;

            }

            try {
                writer = ConfigWriter.getInstance(this.plugin);
            } catch (IOException e) {
                sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
                e.printStackTrace();
                return true;
            }

            try {
                writer.deletePlayerZone(zone.getId());
                writer.writePlayerZone(zone);
                String message = ChatColor.GREEN + "Dem Spieler " + ChatColor.RESET + who.getDisplayName() + ChatColor.GREEN +
                        " wurde das Recht " + ChatColor.DARK_GREEN + args[2].toUpperCase() + ChatColor.GREEN + " verwehrt";
                sender.sendMessage(message);
                return true;
            } catch (IOException e) {
                sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer!");
                e.printStackTrace();
                return true;
            }

        }
        return false;
    }
}

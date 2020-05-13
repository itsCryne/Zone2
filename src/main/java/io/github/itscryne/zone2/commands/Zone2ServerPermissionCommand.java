package io.github.itscryne.zone2.commands;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.config.ConfigWriter;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.perms.PermissionType;
import io.github.itscryne.zone2.spaces.ServerZone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

public class Zone2ServerPermissionCommand implements CommandExecutor { // TODO: * perms
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
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { // add|remove who
                                                                                                   // what id
        if (args.length != 4) { // Subzones
            return false;
        }

        if (!args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("delete")) {
            return false;
        }

        OfflinePlayer who = Bukkit.getPlayer(args[1]);
        if (who == null) {
            who = Bukkit.getOfflinePlayer(args[1]);
        }

        List<String> enumValues = new ArrayList<>(Arrays
                .asList(Arrays.stream(PermissionType.class.getEnumConstants()).map(Enum::name).toArray(String[]::new)));
        boolean validValue = enumValues.stream().anyMatch(args[2]::equalsIgnoreCase);
        if (!validValue) {
            sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("noSuchPerm"));
            sender.sendMessage(ChatColor.DARK_GREEN + Zone2.getPlugin().getConfig().getString("availablePerms"));
            for (String i : enumValues) {
                sender.sendMessage(ChatColor.GREEN + "  - " + i);
            }
            return true;
        }

        int id;
        try {
            id = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Zone2.getPlugin().getConfig().getString("intID"));
            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
            PermissionType permType = PermissionType.valueOf(args[2].toUpperCase(new Locale("de")));
            Permission perm = new Permission(who, permType);

            ConfigReader reader;
            ConfigWriter writer;
            try {
                reader = ConfigReader.getInstance();
            } catch (IOException e) {
                Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
                sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
                return true;
            }

            List<ServerZone> serverZoneList;
            try {
                serverZoneList = reader.getServerZoneList();
            } catch (IOException e) {
                Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
                sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
                ConfigReader.destroy();
                return true;
            }

            if (serverZoneList == null) {
                sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("zoneNotFound"));
                return true;
            }

            ServerZone zone = null;

            for (ServerZone i : serverZoneList) {
                if (id == i.getId()) {
                    zone = i;
                }
            }

            if (zone == null) {
                sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("zoneNotFound"));
                return true;
            }

            Permission zoneAdmin = new Permission((Player) sender, PermissionType.MANAGE);
            if (!zone.getPerms().contains(zoneAdmin) && !sender.hasPermission("zone2.modifyPermissions")) {
                sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("noPermission"));
                return true;
            }

            List<Permission> perms = new ArrayList<>(zone.getPerms());

            for (Permission i : perms) {
                if (i.getP().equals(perm.getP())) {
                    if (i.getPerm().equals(perm.getPerm())) {
                        if (who instanceof Player) {
                            sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("alreadyGranted1") + ChatColor.RESET
                                    + ((Player) who).getDisplayName() + ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("alreadyGranted2")
                                    + ChatColor.RED + args[2].toUpperCase(new Locale("de")) + ChatColor.YELLOW
                                    + Zone2.getPlugin().getConfig().getString("alreadyGranted3"));
                        } else {
                            sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("alreadyGranted1") + ChatColor.RESET
                                    + who.getName() + ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("alreadyGranted2") + ChatColor.RED
                                    + args[2].toUpperCase(new Locale("de")) + ChatColor.YELLOW
                                    + Zone2.getPlugin().getConfig().getString("alreadyGranted3"));
                        }
                        return true;
                    }
                }
            }

            perms.add(perm);
            zone.setPerms(perms);

            try {
                writer = ConfigWriter.getInstance();
            } catch (IOException e) {
                sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
                Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
                return true;
            }

            try {
                writer.deleteServerZone(zone.getId());
                writer.writeServerZone(zone);
                String message;
                if (who instanceof Player) {
                    message = ChatColor.GREEN + Zone2.getPlugin().getConfig().getString("grant1") + ChatColor.RESET + ((Player) who).getDisplayName()
                            + ChatColor.GREEN + Zone2.getPlugin().getConfig().getString("grant2") + ChatColor.DARK_GREEN
                            + args[2].toUpperCase(new Locale("de")) + ChatColor.GREEN + Zone2.getPlugin().getConfig().getString("grant3");
                } else {
                    message = ChatColor.GREEN + Zone2.getPlugin().getConfig().getString("grant1") + ChatColor.RESET + who.getName()
                            + ChatColor.GREEN + Zone2.getPlugin().getConfig().getString("grant2") + ChatColor.DARK_GREEN
                            + args[2].toUpperCase(new Locale("de")) + ChatColor.GREEN + Zone2.getPlugin().getConfig().getString("grant3");
                }
                sender.sendMessage(message);
                return true;
            } catch (IOException e) {
                sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
                Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
                return true;
            }

        } else if (args[0].equalsIgnoreCase("delete")) {
            PermissionType permType = PermissionType.valueOf(args[2].toUpperCase(new Locale("de")));

            ConfigReader reader;
            ConfigWriter writer;
            try {
                reader = ConfigReader.getInstance();
            } catch (IOException e) {
                Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
                sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
                return true;
            }

            List<ServerZone> serverZoneList;
            try {
                serverZoneList = reader.getServerZoneList();
            } catch (IOException e) {
                Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
                sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
                ConfigReader.destroy();
                return true;
            }

            if (serverZoneList == null) {
                sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("zoneNotFound"));
                return true;
            }

            ServerZone zone = null;

            for (ServerZone i : serverZoneList) {
                if (id == i.getId()) {
                    zone = i;
                }
            }

            if (zone == null) {
                sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("zoneNotFound"));
                return true;
            }

            Permission zoneAdmin = new Permission((Player) sender, PermissionType.MANAGE);

            if (!zone.getPerms().contains(zoneAdmin) && !sender.hasPermission("zone2.modifyPermissions")) {
                sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("noPermission"));
                return true;
            }

            List<Permission> perms = new ArrayList<>(zone.getPerms());
            boolean removed = false;
            Permission toRemove = null;

            for (Permission i : perms) {
                if (i.getP().equals(who) && i.getPerm().equals(permType)) {
                    toRemove = i;
                    removed = true;
                }
            }

            if (removed) {
                perms.remove(toRemove);
                zone.setPerms(perms);
            }

            if (!removed) {
                if (who instanceof Player) {
                    sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("playerNoPerm1") + ChatColor.RESET
                            + ((Player) who).getDisplayName() + ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("playerNoPerm2")
                            + ChatColor.RED + args[2].toUpperCase(new Locale("de")) + ChatColor.YELLOW
                            + Zone2.getPlugin().getConfig().getString("playerNoPerm3"));
                } else {
                    sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("playerNoPerm1") + ChatColor.RESET + who.getName()
                            + ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("playerNoPerm2") + ChatColor.RED
                            + args[2].toUpperCase(new Locale("de")) + ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("playerNoPerm3"));
                }
                return true;

            }

            try {
                writer = ConfigWriter.getInstance();
            } catch (IOException e) {
                sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
                Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
                return true;
            }

            try {
                writer.deleteServerZone(zone.getId());
                writer.writeServerZone(zone);
                String message;
                if (who instanceof Player) {
                    message = ChatColor.GREEN + Zone2.getPlugin().getConfig().getString("revoke1") + ChatColor.RESET + ((Player) who).getDisplayName()
                            + ChatColor.GREEN + Zone2.getPlugin().getConfig().getString("revoke2") + ChatColor.DARK_GREEN
                            + args[2].toUpperCase(new Locale("de")) + ChatColor.GREEN + Zone2.getPlugin().getConfig().getString("revoke3");
                } else {
                    message = ChatColor.GREEN + Zone2.getPlugin().getConfig().getString("revoke1") + ChatColor.RESET + who.getName() + ChatColor.GREEN
                            + Zone2.getPlugin().getConfig().getString("revoke2") + ChatColor.DARK_GREEN + args[2].toUpperCase(new Locale("de"))
                            + ChatColor.GREEN + Zone2.getPlugin().getConfig().getString("revoke3");
                }
                sender.sendMessage(message);
                return true;
            } catch (IOException e) {
                sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
                Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
                return true;
            }

        }
        return false;
    }
}

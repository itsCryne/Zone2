package io.github.itscryne.zone2.commands;

import com.sk89q.worldedit.WorldEditException;
import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigWriter;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.Zone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Zone2CreatePlayerZoneCommand implements CommandExecutor {
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
            sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("onlyPlayersZones"));
            return true;
        }

        if (args.length != 4) {
            return false;
        }

        int hx, lx, hz, lz;
        int hy = 256;
        int ly = 0;

        try {
            hx = Integer.max(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            lx = Integer.min(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            hz = Integer.max(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            lz = Integer.min(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("intCoords"));
            return true;
        }

        int minLength = Zone2.getPlugin().getConfig().getInt("minLengthGTH", 20);
        int maxLength = Zone2.getPlugin().getConfig().getInt("maxLengthGTH", 150);

        if (hx - lx < minLength || hz - lz < minLength) {
            sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("tooShort1")
                    + minLength + Zone2.getPlugin().getConfig().getString("tooShort2"));
            return true;
        }
        if (hx - lx > maxLength || hz - lz > maxLength) {
            sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("tooLong1")
                    + maxLength + Zone2.getPlugin().getConfig().getString("tooLong2"));
            return true;
        }

        String world = Zone2.getPlugin().getConfig().getString("world", "world");
        World w = Bukkit.getWorld(world);

        if (w == null) {
            sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
            sender.sendMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("cantCreate"));
            Zone2.getPlugin().getLogger().severe(Zone2.getPlugin().getConfig().getString("invalidConfigWorld"));
            return true;
        }

        int priority = 1;
        int id;
        try {
            id = Zone.getNextId();
        } catch (IOException e) {
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
            sender.sendMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("cantCreate"));
            return true;
        }

        String name = sender.getName();
        UUID uuid = ((Player) sender).getUniqueId();
        List<Permission> perms = new ArrayList<>();
        PlayerZone pz = new PlayerZone(hx, lx, hy, ly, hz, lz, w, priority, id, name, uuid, perms);

        try {
            ConfigWriter writer = ConfigWriter.getInstance();
            if (pz.collidesWithAnyZone()) {
                sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("cantCreateHere"));
                return true;
            }

            String zoneName = "Zone_".concat(name).concat("_").concat(String.valueOf(id)).concat("_")
                    .concat(uuid.toString());

            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        pz.saveSchem(zoneName);
                    } catch (WorldEditException | IOException e) {
                        Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
                        sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
                        sender.sendMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("cantCreate"));
                        return;
                    }
                    pz.displayAreaMarker(Zone2.getMarkers());
                }
            }.runTask(Zone2.getPlugin());

            writer.writePlayerZone(pz);
            sender.sendMessage(ChatColor.GREEN + Zone2.getPlugin().getConfig().getString("success"));
            ConfigWriter.destroy();
            return true;
        } catch (IOException e) {
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
            sender.sendMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("cantCreate"));
            return true;
        }
    }
}

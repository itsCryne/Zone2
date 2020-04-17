package io.github.itscryne.zone2.commands;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigWriter;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.spaces.ServerZone;
import io.github.itscryne.zone2.spaces.Zone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.sk89q.worldedit.WorldEditException;

public class Zone2CreateServerZoneCommand implements CommandExecutor {
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { // hx, lx, hy, ly,
                                                                                                   // hz. lz, w,
                                                                                                   // priority, name
        if (args.length != 9) {
            return false;
        }

        Integer hx = null;
        Integer lx = null;
        Integer hy = null;
        Integer ly = null;
        Integer hz = null;
        Integer lz = null;

        try {
            hx = Integer.max(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            lx = Integer.min(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            hy = Integer.max(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            ly = Integer.min(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            hz = Integer.max(Integer.parseInt(args[4]), Integer.parseInt(args[5]));
            lz = Integer.min(Integer.parseInt(args[4]), Integer.parseInt(args[5]));
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("intCoords"));
            return true;
        }

        World w = Bukkit.getWorld(args[6]);

        if (w == null) {
            sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("noSuchWorld"));
            sender.sendMessage(ChatColor.DARK_GREEN + Zone2.getPlugin().getConfig().getString("possibleWorlds"));
            for (World i : Bukkit.getWorlds()) {
                sender.sendMessage(ChatColor.GREEN + "  - \"" + i.getName() + "\"");
            }
            return true;
        }

        int priority = Integer.parseInt(args[7]);

        int id = 0;
        try {
            id = Zone.getNextId();
        } catch (IOException e) {
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
            sender.sendMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("cantCreate"));
            return true;
        }

        String name = args[8];

        List<Permission> perms = new ArrayList<>();

        ServerZone sz = new ServerZone(hx, lx, hy, ly, hz, lz, w, priority, id, name, perms);

        try {
            ConfigWriter writer = ConfigWriter.getInstance();
            String zoneName = "Serverone_".concat(name).concat("_").concat(String.valueOf(id)).concat("_");
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        sz.saveSchem(zoneName);
                    } catch (WorldEditException | IOException e) {
                        Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
                        sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
                        sender.sendMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("cantCreate"));
                        return;
                    }
                    sz.displayAreaMarker(Zone2.getServerMarkers());
                }
            }.runTask(Zone2.getPlugin());
            writer.writeServerZone(sz);
            sender.sendMessage(ChatColor.GREEN + Zone2.getPlugin().getConfig().getString("success"));
            ConfigWriter.destroy();
            return true;
        } catch (IOException e) {
            sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
            sender.sendMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("cantCreate"));
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            return true;
        }
    }
}

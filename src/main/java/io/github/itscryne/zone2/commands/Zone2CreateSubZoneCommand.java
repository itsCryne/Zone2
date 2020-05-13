package io.github.itscryne.zone2.commands;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigWriter;
import io.github.itscryne.zone2.extensions.ZLocation;
import io.github.itscryne.zone2.extensions.ZPlayer;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.perms.PermissionType;
import io.github.itscryne.zone2.spaces.SubZone;
import io.github.itscryne.zone2.spaces.Zone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Zone2CreateSubZoneCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { // hx, lx, hy, ly,
        // hz, lz
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("onlyPlayersSubZones"));
            return true;
        }

        if (args.length != 6) { //TODO: Allow deletion of Subzones
            return false;
        }

        int hx;
        int lx;
        int hy;
        int ly;
        int hz;
        int lz;

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

        World w = Bukkit.getWorld("world");

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

        ZLocation l1 = new ZLocation(w, hx, hy, hz);
        ZLocation l2 = new ZLocation(w, lx, ly, lz);
        ZPlayer senderZoneler = new ZPlayer((Player) sender);

        try {
            if (!(l1.inPlayerZone() && l2.inPlayerZone())) {
                sender.sendMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("mustBeInZone"));
                return true;
            }

            if (!(l1.getHighestPriorityZone().equals(l2.getHighestPriorityZone()))) {
                sender.sendMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("justInOneZone"));
                return true;
            }

            if (!(senderZoneler.isAllowed(l1, PermissionType.MANAGE)
                    && senderZoneler.isAllowed(l2, PermissionType.MANAGE))) {
                sender.sendMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("noSubZonePermHere"));
                return true;
            }
        } catch (IOException e) {
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
            sender.sendMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("cantCreate"));
            return true;
        }

        String name = sender.getName();
        UUID uuid = ((Player) sender).getUniqueId();
        List<Permission> perms = new ArrayList<>();
        


        int middleX = (l1.getBlockX() + l2.getBlockX())/2;
        int middleY = (l1.getBlockY() + l2.getBlockY())/2;
        int middleZ = (l1.getBlockZ() + l2.getBlockZ()) / 2;
        ZLocation middle = new ZLocation(w, middleX, middleY, middleZ);
        Zone h;
        try {
            h = middle.getHighestPriorityZone();
        } catch (IOException e) {
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            sender.sendMessage(ChatColor.DARK_RED + Zone2.getPlugin().getConfig().getString("oops"));
            sender.sendMessage(ChatColor.RED + Zone2.getPlugin().getConfig().getString("cantCreate"));
            return true;
        }
        
        SubZone sz = new SubZone(hx, lx, hy, ly, hz, lz, w, priority, id, name, uuid, perms, h.getZoneUUID());

        try {
            ConfigWriter writer = ConfigWriter.getInstance();
            if (sz.collidesWithAnySubZone()) {
                sender.sendMessage(ChatColor.YELLOW + Zone2.getPlugin().getConfig().getString("subCollides"));
                return true;
            }
            writer.writeSubZone(sz);
            sender.sendMessage(ChatColor.GREEN + Zone2.getPlugin().getConfig().getString("subSuccess"));
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
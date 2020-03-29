package io.github.itscryne.zone2.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigWriter;
import io.github.itscryne.zone2.extensions.Zonecation;
import io.github.itscryne.zone2.extensions.Zoneler;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.perms.PermissionType;
import io.github.itscryne.zone2.spaces.SubZone;
import io.github.itscryne.zone2.spaces.Zone;

public class Zone2CreateSubZoneCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { // hx, lx, hy, ly,
                                                                                                   // hz, lz
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.YELLOW + "Nur Spieler können Spielerzonen erstellen!");
            return true;
        }

        if (args.length != 6) { //TODO: Allow deletion of Subzones
            return false;
        }

        int hx = 0;
        int lx = 0;
        int hy = 256;
        int ly = 0;
        int hz = 0;
        int lz = 0;

        try {
            hx = Integer.parseInt(args[0]);
            lx = Integer.parseInt(args[1]);
            hy = Integer.parseInt(args[2]);
            ly = Integer.parseInt(args[3]);
            hz = Integer.parseInt(args[4]);
            lz = Integer.parseInt(args[5]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.YELLOW + "Die Koordinaten müssen Ganzzahlen sein!");
            return true;
        }

        if (hx < lx || hz < lz || hy < ly) {
            sender.sendMessage(ChatColor.YELLOW + "h <-> High coordinates | l <-> Low coordinates");
            return true;
        }

        World w = Bukkit.getWorld("world");

        if (w == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer");
            sender.sendMessage(ChatColor.RED + "Zone konnte nicht erstellt werden");
            Zone2.getPlugin().getLogger().severe("Die Standardwelt wurde nicht gefunden!");
            return true;
        }

        int priority = 1;
        int id = 0;
        try {
            id = SubZone.getNextId();
        } catch (IOException e) {
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer");
            sender.sendMessage(ChatColor.RED + "Zone konnte nicht erstellt werden");
            return true;
        }

        Zonecation l1 = new Zonecation(w, hx, hy, hz);
        Zonecation l2 = new Zonecation(w, lx, ly, lz);
        Zoneler senderZoneler = new Zoneler((Player) sender);

        try {
            if (!(l1.inPlayerZone() && l2.inPlayerZone())) {
                sender.sendMessage(ChatColor.RED + "Eine Unterzone kann nur innerhalb einer Zone erstellt werden");
                return true;
            }

            if(!(l1.getHighestPriorityZone().equals(l2.getHighestPriorityZone()))){
                sender.sendMessage(ChatColor.RED + "Eine Subzone darf nur in einer Zone liegen");
                return true;
            }

            if (!(senderZoneler.isAllowed(l1, PermissionType.MANAGE)
                    && senderZoneler.isAllowed(l2, PermissionType.MANAGE))) {
                sender.sendMessage(ChatColor.RED + "Du darfst hier keine Unterzone erstellen");
                return true;
            }
        } catch (IOException e) {
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer");
            sender.sendMessage(ChatColor.RED + "Zone konnte nicht erstellt werden");
            return true;
        }

        String name = ((Player) sender).getName();
        UUID uuid = ((Player) sender).getUniqueId();
        List<Permission> perms = new ArrayList<>();
        


        int middleX = (l1.getBlockX() + l2.getBlockX())/2;
        int middleY = (l1.getBlockY() + l2.getBlockY())/2;
        int middleZ = (l1.getBlockZ() + l2.getBlockZ())/2;
        Zonecation middle = new Zonecation(w, middleX, middleY, middleZ);
        Zone h;
        try {
            h = middle.getHighestPriorityZone();
        } catch (IOException e) {
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer");
            sender.sendMessage(ChatColor.RED + "Zone konnte nicht erstellt werden");
            return true;
        }
        
        SubZone sz = new SubZone(hx, lx, hy, ly, hz, lz, w, priority, id, name, uuid, perms, h.getZoneUUID());

        try {
            ConfigWriter writer = ConfigWriter.getInstance();
            if (sz.collidesWithAnySubZone()) {
                sender.sendMessage(ChatColor.YELLOW + "Die Unterzone kann hier nicht erstellt werden!");
                return true;
            }
            writer.writeSubZone(sz);
            sender.sendMessage(ChatColor.GREEN + "Unterzone wurde erstellt!");
            writer.destroy();
            return true;
        } catch (IOException e) {
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer");
            sender.sendMessage(ChatColor.RED + "Zone konnte nicht erstellt werden");
            return true;
        }
    }
}
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
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.spaces.PlayerZone;

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
            sender.sendMessage(ChatColor.YELLOW + "Nur Spieler können Spielerzonen erstellen!");
            return true;
        }

        if (args.length != 4){
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
            hz = Integer.parseInt(args[2]);
            lz = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.YELLOW + "Die Koordinaten müssen Ganzzahlen sein!");
            return true;
        }

        if (hx < lx || hz < lz) {
            sender.sendMessage(ChatColor.YELLOW + "h <-> High coordinates | l <-> Low coordinates");
            return true;
        }

        final int minLength = Zone2.getPlugin().getConfig().getInt("minLengthGTH", 20);
        final int maxLength = Zone2.getPlugin().getConfig().getInt("maxLengthGTH", 100);

        if (hx - lx < minLength || hz - lz < minLength) {
            sender.sendMessage(ChatColor.YELLOW + "Eine Zone muss in alle Richtungen mindestens "
                    + String.valueOf(minLength) + " Blöcke lang sein");
            return true;
        }
        if (hx - lx > maxLength || hz - lz > maxLength) {
            sender.sendMessage(ChatColor.YELLOW + "Eine Zone darf in alle Richtungen maximal " + String.valueOf(maxLength)
                    + " Blöcke lang sein");
            return true;
        }

        final String world = Zone2.getPlugin().getConfig().getString("world", "world");
        World w = Bukkit.getWorld(world);

        if (w == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer");
            sender.sendMessage(ChatColor.RED + "Zone konnte nicht erstellt werden");
            Zone2.getPlugin().getLogger().severe("Die Standardwelt wurde nicht gefunden!");
            return true;
        }

        int priority = 1;
        int id = 0;
        try {
            id = PlayerZone.getNextId();
        } catch (IOException e) {
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer");
            sender.sendMessage(ChatColor.RED + "Zone konnte nicht erstellt werden");
            return true;
        }

        String name = ((Player) sender).getName();
        UUID uuid = ((Player) sender).getUniqueId();
        List<Permission> perms = new ArrayList<>();
        PlayerZone pz = new PlayerZone(hx, lx, hy, ly, hz, lz, w, priority, id, name, uuid, perms);

        try {
            ConfigWriter writer = ConfigWriter.getInstance();
            if (pz.collidesWithAnyZone()) {
                sender.sendMessage(ChatColor.YELLOW + "Die Zone kann hier nicht erstellt werden!");
                return true;
            }
            writer.writePlayerZone(pz);
            sender.sendMessage(ChatColor.GREEN + "Zone wurde erstellt!");
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

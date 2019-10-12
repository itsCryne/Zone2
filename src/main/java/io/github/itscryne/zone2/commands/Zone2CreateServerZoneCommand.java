package io.github.itscryne.zone2.commands;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigWriter;
import io.github.itscryne.zone2.spaces.ServerZone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class Zone2CreateServerZoneCommand implements CommandExecutor {
    private Zone2 plugin;

    public Zone2CreateServerZoneCommand(Zone2 plugin){
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { //hx, lx, hy, ly, hz. lz, w, priority, name
        if (args.length != 9) { return false; }

        Integer hx = null;
        Integer lx = null;
        Integer hy = null;
        Integer ly = null;
        Integer hz = null;
        Integer lz = null;

        try {
            hx = Integer.parseInt(args[0]);
            lx = Integer.parseInt(args[1]);
            hy = Integer.parseInt(args[2]);
            ly = Integer.parseInt(args[3]);
            hz = Integer.parseInt(args[4]);
            lz = Integer.parseInt(args[5]);
        } catch (NumberFormatException e){
            sender.sendMessage(ChatColor.YELLOW + "Die Koordinaten müssen Ganzzahlen sein!");
            return true;
        }

        World w = Bukkit.getWorld(args[6]);

        if (w == null){
            sender.sendMessage(ChatColor.YELLOW + "Diese Welt existiert nicht!");
            sender.sendMessage(ChatColor.DARK_GREEN + "Mögliche Welten sind:");
            for (World i : Bukkit.getWorlds()){
                sender.sendMessage(ChatColor.GREEN + "  - \"" + i.getName() + "\"");
            }
            return true;
        }

        int priority = Integer.parseInt(args[7]);

        int id = 0;
        try {
            id = ServerZone.getNextId(this.plugin);
        } catch (IOException e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer");
            sender.sendMessage(ChatColor.RED + "Zone konnte nicht erstellt werden");
            return true;
        }

        String name = args[8];

        ServerZone sz = new ServerZone(hx, lx, hy, ly, hz, lz, w, priority, id, name);

        try {
            ConfigWriter writer = ConfigWriter.getInstance(this.plugin);
            writer.writeServerZone(sz);
            sender.sendMessage("Zone wurde erstellt!");
            return true;
        } catch (IOException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer");
            sender.sendMessage(ChatColor.RED + "Zone konnte nicht erstellt werden");
            e.printStackTrace();
            return true;
        }
    }
}

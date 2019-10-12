package io.github.itscryne.zone2.commands;

import io.github.itscryne.zone2.*;
import io.github.itscryne.zone2.config.ConfigWriter;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.perms.PermissionType;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.ServerZone;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class Zone2CommandExecutor implements CommandExecutor {
    private Zone2 plugin;
    private Logger logger;

    public Zone2CommandExecutor(Zone2 plugin) {
        this.plugin = plugin;
        this.logger = this.plugin.getLogger();
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
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl benutzen");
            return true;
        }

        World w = this.plugin.getServer().getWorld("world");
        Location l1 = new Location(w, 100, 256, 100);
        Location l2 = new Location(w, -100, 0, -100);

        UUID senderUuid = ((Player) sender).getUniqueId();

        List<Permission> perms = new ArrayList<>();
        Permission p = new Permission((Player) sender, PermissionType.ADMINISTRATE);
        perms.add(p);

        PlayerZone testPlayerZone = new PlayerZone(l1, l2, 1, 1, sender.getName(), senderUuid, perms);
        ServerZone testServerZone = new ServerZone(l1, l2, 1, 0, "Test");

        ConfigWriter writer = null;

        try {
            if (PlayerZone.canCreate((Player) sender, this.plugin) && !testPlayerZone.collidesWithAnyZone(this.plugin)) {
                writer = ConfigWriter.getInstance(this.plugin);
                writer.writePlayerZone(testPlayerZone);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.destroy();
            }
        }
        return false;
    }
}

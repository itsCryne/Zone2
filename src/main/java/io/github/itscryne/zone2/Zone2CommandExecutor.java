package io.github.itscryne.zone2;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zone2CommandExecutor implements CommandExecutor {
    private Zone2 plugin;

    public Zone2CommandExecutor(Zone2 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("test")){
            if (!(sender instanceof Player)){
                return false;
            }
            World w = sender.getServer().getWorld("world");
            Location l1 = new Location(w, 100, 0, 100);
            Location l2 = new Location(w, -100, 256, -100);

            List<Permission> perms = new ArrayList<>();
            perms.add(new Permission((Player)sender, PermissionType.ADMINISTRATE));

            PlayerZone testZone = new PlayerZone(l1, l2, 1, 1, sender.getName(), ((Player)sender).getUniqueId(), perms);

            try {
                this.plugin.getLogger().info("Before");
                ConfigWriter testWriter = new ConfigWriter(this.plugin);
                this.plugin.getLogger().info("After");
                testWriter.writePlayerZone(testZone);
            } catch (IOException ignored) {
            }
            return true;
        }
        return false;
    }
}

package io.github.itscryne.zone2;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zone2CommandExecutor implements CommandExecutor{
    private Zone2 plugin;

    public Zone2CommandExecutor(Zone2 plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Nur Spieler können diesen Befehl benutzen");
            return true;
        }

        ConfigWriter writer = null;
        ConfigReader reader = null;

        //These values are only for demonstration and testing purposes! Some values must be unique in production!
        World w = this.plugin.getServer().getWorld("world");
        Location l1 = new Location(w, 100, 256, 100);
        Location l2 = new Location(w, -100, 0, -100);

        Permission perm = new Permission((Player) sender, PermissionType.ADMINISTRATE);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(perm);


        try{
            writer = new ConfigWriter(this.plugin);
            reader = new ConfigReader(this.plugin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (command.getName().toLowerCase()){
            case ("createplayerzone"):
                PlayerZone playerZone = new PlayerZone(l1, l2, 1, 1, "PlayerZone", ((Player) sender).getUniqueId(), permissions);
                try {
                    writer.writePlayerZone(playerZone);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case ("getplayerzone"):
                try {
                    List<PlayerZone> playerZoneList = reader.getPlayerZoneList();
                    for (PlayerZone i : playerZoneList){
                        System.out.println(i);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case ("deleteplayerzone"):
                try {
                    writer.deletePlayerZone(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case ("createserverzone"):
                ServerZone serverZone = new ServerZone(l1, l2, 1, 1, "ServerZone");
                try {
                    writer.writeServerZone(serverZone);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case ("getserverzone"):
                try {
                    List<ServerZone> serverZoneList = reader.getServerZoneList();
                    for (ServerZone i : serverZoneList){
                        System.out.println(i);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case ("deleteserverzone"):
                try {
                    writer.deleteServerZone(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                return false;
        }
        return true;
    }
}

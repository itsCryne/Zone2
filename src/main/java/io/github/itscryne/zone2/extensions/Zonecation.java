package io.github.itscryne.zone2.extensions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.ServerZone;

public class Zonecation extends Location {

    public Zonecation(World world, double x, double y, double z) {
        super(world, x, y, z);
    }
    public Zonecation(World world, double x, double y, double z, float yaw, float pitch){
        super(world, x, y, z, yaw, pitch);
    }

    public Zonecation(Location l){
        super(l.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
    }

    public boolean inPlayerZone() throws FileNotFoundException, IOException {
        for (PlayerZone i : ConfigReader.getInstance(Zone2.getPlugin()).getPlayerZoneList()){
            if (i.contains(this)){
                return true;
            }
        }
        return false;
    }

    public boolean inServerZone() throws FileNotFoundException, IOException {
        for (ServerZone i : ConfigReader.getInstance(Zone2.getPlugin()).getServerZoneList()){
            if (i.contains(this)){
                return true;
            }
        }
        return false;
    }

    public boolean inZone() throws FileNotFoundException, IOException {
        return this.inPlayerZone() || this.inServerZone();
    }

    public List<ServerZone> getServerZones() throws FileNotFoundException, IOException {
        List<ServerZone> serverZoneList = new ArrayList<>();
        for(ServerZone i : ConfigReader.getInstance(Zone2.getPlugin()).getServerZoneList()){ //TODO: Remove Plugin
            if (i.contains(this)){
                serverZoneList.add(i);
            }
        }
        return serverZoneList;
    }

    public List<PlayerZone> getPlayerZones() throws FileNotFoundException, IOException {
        List<PlayerZone> playerZoneList = new ArrayList<>();
        for(PlayerZone i : ConfigReader.getInstance(Zone2.getPlugin()).getPlayerZoneList()){ //TODO: Remove Plugin
            if (i.contains(this)){
                playerZoneList.add(i);
            }
        }
        return playerZoneList;
    }

}
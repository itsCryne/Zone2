package io.github.itscryne.zone2.extensions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.ServerZone;
import io.github.itscryne.zone2.spaces.SubZone;
import io.github.itscryne.zone2.spaces.Zone;

public class Zonecation extends Location {

    public Zonecation(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public Zonecation(World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
    }

    public Zonecation(Location l) {
        super(l.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
    }

    public boolean inPlayerZone() throws FileNotFoundException, IOException {
        for (PlayerZone i : ConfigReader.getInstance().getPlayerZoneList()) {
            if (i.contains(this)) {
                return true;
            }
        }
        return false;
    }

    public boolean inServerZone() throws FileNotFoundException, IOException {
        for (ServerZone i : ConfigReader.getInstance().getServerZoneList()) {
            if (i.contains(this)) {
                return true;
            }
        }
        return false;
    }

    public boolean inZone() throws FileNotFoundException, IOException {
        return this.inPlayerZone() || this.inServerZone();
    }

    public boolean inSubZone() throws FileNotFoundException, IOException {
        for (SubZone i : ConfigReader.getInstance().getSubZoneList()) {
            if (i.contains(this)) {
                return true;
            }
        }
        return false;
    }

    public List<ServerZone> getServerZones() throws FileNotFoundException, IOException {
        List<ServerZone> serverZoneList = new ArrayList<>();
        for (ServerZone i : ConfigReader.getInstance().getServerZoneList()) {
            if (i.contains(this)) {
                serverZoneList.add(i);
            }
        }
        return serverZoneList;
    }

    public List<PlayerZone> getPlayerZones() throws FileNotFoundException, IOException {
        List<PlayerZone> playerZoneList = new ArrayList<>();
        for (PlayerZone i : ConfigReader.getInstance().getPlayerZoneList()) {
            if (i.contains(this)) {
                playerZoneList.add(i);
            }
        }
        return playerZoneList;
    }

    public List<SubZone> getSubZones() throws FileNotFoundException, IOException {
        List<SubZone> subZoneList = new ArrayList<>();
        for (SubZone i : ConfigReader.getInstance().getSubZoneList()) {
            if (i.contains(this)) {
                subZoneList.add(i);
            }
        }
        return subZoneList;
    }

    public PlayerZone getHighestPriorityPlayerZone() throws FileNotFoundException, IOException {
        PlayerZone highestPriorityZone = null;
        for(PlayerZone z : this.getPlayerZones()){
            if (highestPriorityZone == null) {
                highestPriorityZone = z;
            }
            if (highestPriorityZone.getPriority() < z.getPriority()) {
                highestPriorityZone = z;
            }
        }
        return highestPriorityZone;
    }

    public ServerZone getHighestPriorityServerZone() throws FileNotFoundException, IOException {
        ServerZone highestPriorityZone = null;
        for(ServerZone z : this.getServerZones()){
            if (highestPriorityZone == null) {
                highestPriorityZone = z;
            }
            if (highestPriorityZone.getPriority() < z.getPriority()) {
                highestPriorityZone = z;
            }
        }
        return highestPriorityZone;
    }

    public SubZone getHighestPrioritySubZone() throws FileNotFoundException, IOException {
        SubZone highestPriorityZone = null;
        for(SubZone z : this.getSubZones()){
            if (highestPriorityZone == null) {
                highestPriorityZone = z;
            }
            if (highestPriorityZone.getPriority() < z.getPriority()) {
                highestPriorityZone = z;
            }
        }
        return highestPriorityZone;
    }

    public Zone getHighestPriorityZone() throws FileNotFoundException, IOException {
        PlayerZone p = this.getHighestPriorityPlayerZone();
        ServerZone s = this.getHighestPriorityServerZone();

        if(p == null){
            return s;
        }
        if(s == null){
            return p;
        }

        if(p.getPriority() > s.getPriority()){
            return p;
        } else {
            return s;
        }
    }
}
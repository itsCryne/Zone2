package io.github.itscryne.zone2.extensions;

import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.ServerZone;
import io.github.itscryne.zone2.spaces.SubZone;
import io.github.itscryne.zone2.spaces.Zone;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZLocation extends Location {

    public ZLocation(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public ZLocation(Location l) {
        super(l.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
    }

    public boolean inPlayerZone() throws IOException {
        for (PlayerZone i : ConfigReader.getInstance().getPlayerZoneList()) {
            if (i.contains(this)) {
                return true;
            }
        }
        return false;
    }

    public boolean inServerZone() throws IOException {
        for (ServerZone i : ConfigReader.getInstance().getServerZoneList()) {
            if (i.contains(this)) {
                return true;
            }
        }
        return false;
    }

    public boolean inZone() throws IOException {
        return this.inPlayerZone() || this.inServerZone();
    }

    public boolean inSubZone() throws IOException {
        for (SubZone i : ConfigReader.getInstance().getSubZoneList()) {
            if (i.contains(this)) {
                return true;
            }
        }
        return false;
    }

    public List<ServerZone> getServerZones() throws IOException {
        List<ServerZone> serverZoneList = new ArrayList<>();
        for (ServerZone i : ConfigReader.getInstance().getServerZoneList()) {
            if (i.contains(this)) {
                serverZoneList.add(i);
            }
        }
        return serverZoneList;
    }

    public List<PlayerZone> getPlayerZones() throws IOException {
        List<PlayerZone> playerZoneList = new ArrayList<>();
        for (PlayerZone i : ConfigReader.getInstance().getPlayerZoneList()) {
            if (i.contains(this)) {
                playerZoneList.add(i);
            }
        }
        return playerZoneList;
    }

    public List<SubZone> getSubZones() throws IOException {
        List<SubZone> subZoneList = new ArrayList<>();
        for (SubZone i : ConfigReader.getInstance().getSubZoneList()) {
            if (i.contains(this)) {
                subZoneList.add(i);
            }
        }
        return subZoneList;
    }

    public PlayerZone getHighestPriorityPlayerZone() throws IOException {
        PlayerZone highestPriorityZone = null;
        for (PlayerZone z : this.getPlayerZones()) {
            if (highestPriorityZone == null) {
                highestPriorityZone = z;
            }
            if (highestPriorityZone.getPriority() < z.getPriority()) {
                highestPriorityZone = z;
            }
        }
        return highestPriorityZone;
    }

    public ServerZone getHighestPriorityServerZone() throws IOException {
        ServerZone highestPriorityZone = null;
        for (ServerZone z : this.getServerZones()) {
            if (highestPriorityZone == null) {
                highestPriorityZone = z;
            }
            if (highestPriorityZone.getPriority() < z.getPriority()) {
                highestPriorityZone = z;
            }
        }
        return highestPriorityZone;
    }

    public SubZone getHighestPrioritySubZone() throws IOException {
        SubZone highestPriorityZone = null;
        for (SubZone z : this.getSubZones()) {
            if (highestPriorityZone == null) {
                highestPriorityZone = z;
            }
            if (highestPriorityZone.getPriority() < z.getPriority()) {
                highestPriorityZone = z;
            }
        }
        return highestPriorityZone;
    }

    public Zone getHighestPriorityZone() throws IOException {
        PlayerZone p = this.getHighestPriorityPlayerZone();
        ServerZone s = this.getHighestPriorityServerZone();

        if (p == null) {
            return s;
        }
        if (s == null) {
            return p;
        }

        if(p.getPriority() > s.getPriority()){
            return p;
        } else {
            return s;
        }
    }
}
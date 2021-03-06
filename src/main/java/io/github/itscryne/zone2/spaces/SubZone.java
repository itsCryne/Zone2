package io.github.itscryne.zone2.spaces;

import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.extensions.ZLocation;
import io.github.itscryne.zone2.perms.Permission;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @serial JSON
 */
public class SubZone extends Zone {
    private final UUID playerUUID;
    private final UUID superZoneUUID;

    public SubZone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id, String name,
                   UUID playerUuid, List<Permission> perms, UUID superZoneUUID) {
        super(hx, lx, hy, ly, hz, lz, w, priority, id, name, perms);
        this.playerUUID = playerUuid;
        this.superZoneUUID = superZoneUUID;
    }

    // TODO: Are Subzones always preferred over normal Zones?

    public boolean collidesWithAnySubZone() throws IOException {
        ConfigReader reader = ConfigReader.getInstance();
        List<SubZone> subZoneList = reader.getSubZoneList();

        if (subZoneList == null || subZoneList.isEmpty()) {
            return false;
        }

        for (SubZone i : subZoneList) {
            i.setL1(new ZLocation(Location.deserialize(i.getSerL1())));
            i.setL2(new ZLocation(Location.deserialize(i.getSerL2())));

            if (this.getL2().getX() <= i.getL1().getX() && this.getL1().getX() >= i.getL2().getX()
                    && this.getL2().getY() <= i.getL1().getY() && this.getL1().getY() >= i.getL2().getY()
                    && this.getL2().getZ() <= i.getL1().getZ() && this.getL1().getZ() >= i.getL2().getZ()
                    && this.getL1().getWorld().equals(i.getL1().getWorld())) {
                return true;
            }
        }
        return false;
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }
    public UUID getSuperZoneUUID() {
        return this.superZoneUUID;
    }
}
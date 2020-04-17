package io.github.itscryne.zone2.spaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;

import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.extensions.Zonecation;
import io.github.itscryne.zone2.perms.Permission;

/**
 * @serial JSON
 */
public class SubZone extends Zone {
    private UUID playerUUID;
    private UUID superZoneUUID;

    public SubZone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id, String name,
            UUID playerUuid, List<Permission> perms, UUID superZoneUUID) {
        super(hx, lx, hy, ly, hz, lz, w, priority, id, name, perms);
        this.playerUUID = playerUuid;
        this.superZoneUUID = superZoneUUID;
    }

    // TODO: Are Subzones always preferred over normal Zones?

    public SubZone(Zonecation l1, Zonecation l2, int priority, int id, String name, UUID playerUuid,
            List<Permission> perms, UUID superZoneUUID) {
        super(l1, l2, priority, id, name, perms);
        this.playerUUID = playerUuid;
        this.superZoneUUID = superZoneUUID;
    }

    public boolean collidesWithAnySubZone() throws IOException {
        ConfigReader reader = ConfigReader.getInstance();
        List<SubZone> subZoneList = reader.getSubZoneList();

        if (subZoneList == null || subZoneList.isEmpty()) {
            return false;
        }

        if (!subZoneList.isEmpty()) {
            for (SubZone i : subZoneList) {
                i.setL1(new Zonecation(Location.deserialize(i.getSerL1())));
                i.setL2(new Zonecation(Location.deserialize(i.getSerL2())));

                if (this.getL2().getX() <= i.getL1().getX() && this.getL1().getX() >= i.getL2().getX()
                        && this.getL2().getY() <= i.getL1().getY() && this.getL1().getY() >= i.getL2().getY()
                        && this.getL2().getZ() <= i.getL1().getZ() && this.getL1().getZ() >= i.getL2().getZ()
                        && this.getL1().getWorld().equals(i.getL1().getWorld())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<SubZone> getSubZones() {
        return new ArrayList<SubZone>();
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public UUID getSuperZoneUUID() {
        return this.superZoneUUID;
    }
}
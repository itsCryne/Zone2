package io.github.itscryne.zone2.spaces;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.extensions.Zonecation;

/**
 * @serial JSON
 */
public class Area implements Serializable {
    private transient Zonecation l1; //upper Location
    private transient Zonecation l2; //lower Location
    private Map<String, Object> serL1;
    private Map<String, Object> serL2;

    /**
     * @param l1 First Location (higher coordinates)
     * @param l2 Second Location (lower coordinates)
     */
    protected Area(Zonecation l1, Zonecation l2) {
        this.l1 = l1;
        this.l2 = l2;
        this.serL1 = this.l1.serialize();
        this.serL2 = this.l2.serialize();

        if (!(this.l1.getWorld().equals(this.l2.getWorld()))) {
            throw new RuntimeException("Worlds do not match");
        }

        if (this.l1.getBlockX() < this.l2.getBlockX()) {
            throw new RuntimeException("l1 must contain the higher coordinates");
        } else if (this.l1.getBlockY() < this.l1.getBlockY()) {
            throw new RuntimeException("l1 must conatin the higher coordinates");
        } else if (this.l1.getBlockZ() < this.l2.getBlockZ()) {
            throw new RuntimeException("l1 must contain the higher coordinates");
        }
    }

    /**
     * @param hx Hgher x coordinate
     * @param lx Lower x coordinate
     * @param hy Higher y coordinate
     * @param ly Lower l coordinate
     * @param hz Higher z coordinate
     * @param lz Lower z coordinate
     * @param w  World
     */
    protected Area(int hx, int lx, int hy, int ly, int hz, int lz, World w) {
        this.l1 = new Zonecation(w, hx+1, hy+1, hz+1);
        this.l2 = new Zonecation(w, lx-1, ly-1, lz-1);
        this.serL1 = this.l1.serialize();
        this.serL2 = this.l2.serialize();

        if (this.l1.getBlockX() < this.l2.getBlockX()) {
            throw new RuntimeException("l1 must contain the higher coordinates");
        } else if (this.l1.getBlockY() < this.l2.getBlockY()) {
            throw new RuntimeException("l1 must conatin the higher coordinates");
        } else if (this.l1.getBlockZ() < this.l2.getBlockZ()) {
            throw new RuntimeException("l1 must contain the higher coordinates");
        }
    }

    /**
     * @param l Location to test
     * @return Wether the Zone object contains l
     */
    public boolean contains(Zonecation l) {
        if (this.l1 == null) {
            this.l1 = new Zonecation(Location.deserialize(serL1));
        }
        if (this.l2 == null) {
            this.l2 = new Zonecation(Location.deserialize(serL2));
        }

        boolean worldsMatch = l.getWorld().equals(this.getL1().getWorld());
        boolean xContain = l.getBlockX() < this.l1.getBlockX() && l.getBlockX() > this.l2.getBlockX();
        boolean yContain = l.getBlockY() < this.l1.getBlockY() && l.getBlockY() > this.l2.getBlockY();
        boolean zContain = l.getBlockZ() < this.l1.getBlockZ() && l.getBlockZ() > this.l2.getBlockZ();

        return xContain && yContain && zContain && worldsMatch;
    }

    public void saveSchem(String name) throws WorldEditException, IOException {
        if (this.l1 == null) {
            this.l1 = new Zonecation(Location.deserialize(serL1));
        }
        if (this.l2 == null) {
            this.l2 = new Zonecation(Location.deserialize(serL2));
        }

        World world = Bukkit.getWorld("world");
        com.sk89q.worldedit.world.World  weWorld = new com.sk89q.worldedit.bukkit.BukkitWorld(world);
        BlockVector3 min = BlockVector3.at(this.l1.getX(),this.l1.getY(),this.l1.getZ());
        BlockVector3 max = BlockVector3.at(this.l2.getX(),this.l2.getY(),this.l2.getZ());
        CuboidRegion region = new CuboidRegion(weWorld, min, max);
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(region.getWorld(), -1);

        ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
        forwardExtentCopy.setCopyingEntities(true);
        Operations.complete(forwardExtentCopy);

        new File(Zone2.getPlugin().getDataFolder() + "/schematics").mkdirs();
        File saveTo = new File(Zone2.getPlugin().getDataFolder() + "/schematics/" + name + ".schem");

        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(saveTo))) {
            writer.write(clipboard);
        }
    }

    /**
     * @return First Location (higher coordinates)
     */
    public Zonecation getL1() {
        return this.l1;
    }

    /**
     *
     * @param l1 The Location to set
     */
    public void setL1(Zonecation l1) {
        this.l1 = l1;
    }

    /**
     * @return Second Location (lower coordinates)
     */
    public Zonecation getL2() {
        return this.l2;
    }

    /**
     *
     * @param l2 The Location to set
     */
    public void setL2(Zonecation l2) {
        this.l2 = l2;
    }

    /**
     *
     * @return the serialized L1
     */
    public Map<String, Object> getSerL1() {
        return serL1;
    }

    /**
     *
     * @return the serialized L2
     */
    public Map<String, Object> getSerL2() {
        return serL2;
    }

    public Integer getMaxX(){
        return this.getL1().getBlockX();
    }

    public Integer getMaxZ(){
        return this.getL1().getBlockZ();
    }

    public Integer getMinX(){
        return this.getL2().getBlockX();
    }

    public Integer getMinZ(){
        return this.getL2().getBlockZ();
    }
}

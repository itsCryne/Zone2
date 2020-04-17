package io.github.itscryne.zone2.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.bukkit.Location;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.extensions.Zonecation;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.ServerZone;
import io.github.itscryne.zone2.spaces.SubZone;

public final class ConfigReader {
    private static ConfigReader instance;

    private String playerZonePath;
    private String serverZonePath;
    private String subZonePath;
    private Zone2 plugin;
    private File dataDir;
    private File playerZonesFile;
    private File serverZonesFile;
    private File subZonesFile;

    /**
     * ConfigReader constructor - access via {@link #getInstance(Zone2)} method.
     * 
     * @throws IOException if it cant access the files et al
     */
    private ConfigReader() throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();

        this.plugin = Zone2.getPlugin();
        this.dataDir = this.plugin.getDataFolder();
        dataDir.mkdir();

        String dataDirPath = dataDir.getAbsolutePath();
        this.serverZonePath = dataDirPath.concat(File.separator).concat("serverZones.json");
        this.playerZonePath = dataDirPath.concat(File.separator).concat("playerZones.json");
        this.subZonePath = dataDirPath.concat(File.separator).concat("subZones.json");

        this.playerZonesFile = new File(playerZonePath);
        playerZonesFile.createNewFile();
        if (playerZonesFile.length() == 0) { // writing empty List<PlayerZone> to JSON file so we can access it later
            List<PlayerZone> playerZoneList = new ArrayList<>();

            try (BufferedWriter fw = new BufferedWriter(new FileWriter(playerZonePath))){
            gson.toJson(playerZoneList, fw);
            fw.flush();
            }
        }

        this.serverZonesFile = new File(serverZonePath);
        serverZonesFile.createNewFile();
        if (serverZonesFile.length() == 0) { // writing empty List<ServerZone> to JSON file so we can access it later
            List<ServerZone> serverZoneList = new ArrayList<>();
            try (BufferedWriter fw = new BufferedWriter(new FileWriter(serverZonePath))){
                gson.toJson(serverZoneList, fw);
                fw.flush();
                }
        }

        this.subZonesFile = new File(subZonePath);
        subZonesFile.createNewFile();
        if (subZonesFile.length() == 0) { // writing empty List<ServerZone> to JSON file so we can access it later
            List<SubZone> subZoneList = new ArrayList<>();

            try (BufferedWriter fw = new BufferedWriter(new FileWriter(subZonePath))){
                gson.toJson(subZoneList, fw);
                fw.flush();
                }
        }
    }

    /**
     * Gets a instance of {@link ConfigReader}
     * 
     * @return a ConfigReader instance
     * @throws IOException if it cant access the files et al
     */
    public static synchronized ConfigReader getInstance() throws IOException {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    /**
     * Destroys the ConfigReader instance
     */
    public static void destroy() {
        instance = null;
    }

    /**
     *
     * @return a list of all PlayerZones saved in the json file
     * @throws IOException
     */
    public List<PlayerZone> getPlayerZoneList() throws IOException {
        Gson gson = new Gson();
        FileReader fr = new FileReader(this.playerZonesFile);
        JsonReader playerZoneListReader = new JsonReader(fr);

        Type playerZoneListType = new TypeToken<List<PlayerZone>>() {
        }.getType();

        List<PlayerZone> pzl = gson.fromJson(playerZoneListReader, playerZoneListType);

        if (pzl == null) {
            pzl = new ArrayList<PlayerZone>();
        }

        for (int i = 0; i < pzl.size(); i++) {
            PlayerZone j = pzl.get(i);
            j.setL1(new Zonecation(Location.deserialize(j.getSerL1())));
            j.setL2(new Zonecation(Location.deserialize(j.getSerL2())));
            pzl.set(i, j);
        }

        fr.close();
        return pzl;
    }

    /**
     *
     * @return a list of all ServerZones saved in the json file
     * @throws IOException
     */
    public List<ServerZone> getServerZoneList() throws IOException {
        Gson gson = new Gson();
        FileReader fr = new FileReader(this.serverZonesFile);
        JsonReader serverZoneListReader = new JsonReader(fr);

        Type serverZoneListType = new TypeToken<List<ServerZone>>() {
        }.getType();

        List<ServerZone> szl = gson.fromJson(serverZoneListReader, serverZoneListType);
        for (int i = 0; i < szl.size(); i++) {
            ServerZone j = szl.get(i);
            j.setL1(new Zonecation(Location.deserialize(j.getSerL1())));
            j.setL2(new Zonecation(Location.deserialize(j.getSerL2())));
            szl.set(i, j);
        }

        fr.close();
        return szl;
    }

    public List<SubZone> getSubZoneList() throws IOException {
        Gson gson = new Gson();
        FileReader fr = new FileReader(this.subZonesFile);
        JsonReader serverZoneListReader = new JsonReader(fr);

        Type subZoneListType = new TypeToken<List<SubZone>>() {
        }.getType();

        List<SubZone> szl = gson.fromJson(serverZoneListReader, subZoneListType);
        for (int i = 0; i < szl.size(); i++) {
            SubZone j = szl.get(i);
            j.setL1(new Zonecation(Location.deserialize(j.getSerL1())));
            j.setL2(new Zonecation(Location.deserialize(j.getSerL2())));
            szl.set(i, j);
        }

        fr.close();
        return szl;
    }
}
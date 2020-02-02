package io.github.itscryne.zone2.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.ServerZone;
import io.github.itscryne.zone2.extensions.Zonecation;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConfigReader {
    private static ConfigReader instance;

    private Zone2 plugin;
    private File dataDir;
    private File playerZonesFile;
    private File serverZonesFile;

    /**
     * ConfigReader constructor - access via {@link #getInstance(Zone2)} method.
     * @param plugin the plugin
     * @throws IOException if it cant access the files et al
     */
    private ConfigReader(Zone2 plugin) throws IOException {
        this.plugin = plugin;
        this.dataDir = this.plugin.getDataFolder();
        dataDir.mkdir();

        this.playerZonesFile = new File(dataDir.getAbsolutePath().concat(File.separator).concat("playerZones.json"));
        playerZonesFile.createNewFile();
        if (playerZonesFile.length() == 0) { //writing empty List<PlayerZone> to JSON file so we can access it later
            Type playerZoneListType = new TypeToken<List<PlayerZone>>() {}.getType();
            List<PlayerZone> playerZoneList = new ArrayList<>();

            Gson gson = new Gson();

            JsonWriter playerZoneListWriter = new JsonWriter(new FileWriter(this.playerZonesFile));
            gson.toJson(playerZoneList, playerZoneListType, playerZoneListWriter);
        }

        this.serverZonesFile = new File(dataDir.getAbsolutePath().concat(File.separator).concat("serverZones.json"));
        serverZonesFile.createNewFile();
        if (serverZonesFile.length() == 0) { //writing empty List<ServerZone> to JSON file so we can access it later
            Type serverZoneListType = new TypeToken<List<ServerZone>>() {
            }.getType();
            List<ServerZone> serverZoneList = new ArrayList<>();

            Gson gson = new Gson();

            JsonWriter serverZoneListWriter = new JsonWriter(new FileWriter(this.serverZonesFile));
            gson.toJson(serverZoneList, serverZoneListType, serverZoneListWriter);
        }
    }

    /**
     * Gets a instance of {@link ConfigReader}
     * @param plugin the plugin
     * @return a ConfigReader instance
     * @throws IOException if it cant access the files et al
     */
    public static ConfigReader getInstance(Zone2 plugin) throws IOException {
        if (instance == null) {
            instance = new ConfigReader(plugin);
        }
        return instance;
    }

    /**
     * Destroys the ConfigReader instance
     */
    public void destroy() {
        instance = null;
    }

    /**
     *
     * @return a list of all PlayerZones saved in the json file
     * @throws FileNotFoundException if the file is not found
     */
    public List<PlayerZone> getPlayerZoneList() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader playerZoneListReader = new JsonReader(new FileReader(this.playerZonesFile));

        Type playerZoneListType = new TypeToken<List<PlayerZone>>() {
        }.getType();

        List<PlayerZone> pzl = gson.fromJson(playerZoneListReader, playerZoneListType);

        if (pzl == null){
            pzl = new ArrayList<PlayerZone>();
        }

        for (int i = 0; i < pzl.size(); i++) {
            PlayerZone j = pzl.get(i);
            j.setL1(new Zonecation(Zonecation.deserialize(j.getSerL1())));
            j.setL2(new Zonecation( Zonecation.deserialize(j.getSerL2())));
            pzl.set(i, j);
        }

        return pzl;
    }

    /**
     *
     * @return a list of all ServerZones saved in the json file
     * @throws FileNotFoundException if the file is not found
     */
    public List<ServerZone> getServerZoneList() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader serverZoneListReader = new JsonReader(new FileReader(this.serverZonesFile));

        Type serverZoneListType = new TypeToken<List<ServerZone>>() {
        }.getType();

        List<ServerZone> szl = gson.fromJson(serverZoneListReader, serverZoneListType);
        for (int i = 0; i < szl.size(); i++) {
            ServerZone j = szl.get(i);
            j.setL1(new Zonecation(Zonecation.deserialize(j.getSerL1())));
            j.setL2(new Zonecation( Zonecation.deserialize(j.getSerL2())));
            szl.set(i, j);
        }
        
        return szl;
    }
}

package io.github.itscryne.zone2.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

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

    private final String playerZonePath;
    private final String serverZonePath;
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
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();

        this.plugin = plugin;
        this.dataDir = this.plugin.getDataFolder();
        dataDir.mkdir();

        String dataDirPath = dataDir.getAbsolutePath();
        this.serverZonePath = dataDirPath.concat(File.separator).concat("serverZones.json");
        this.playerZonePath = dataDirPath.concat(File.separator).concat("playerZones.json");

        this.playerZonesFile = new File(playerZonePath);
        playerZonesFile.createNewFile();
        if (playerZonesFile.length() == 0) { //writing empty List<PlayerZone> to JSON file so we can access it later
            List<PlayerZone> playerZoneList = new ArrayList<>();

            Writer fw = new FileWriter(playerZonePath);
            gson.toJson(playerZoneList, fw);
            fw.flush();
            fw.close();
        }

        this.serverZonesFile = new File(serverZonePath);
        serverZonesFile.createNewFile();
        if (serverZonesFile.length() == 0) { //writing empty List<ServerZone> to JSON file so we can access it later
            List<ServerZone> serverZoneList = new ArrayList<>();

            Writer fw = new FileWriter(serverZonePath);
            gson.toJson(serverZoneList, fw);
            fw.flush();
            fw.close();
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

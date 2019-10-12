package io.github.itscryne.zone2.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.ServerZone;

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

    private ConfigReader(Zone2 plugin) throws IOException {
        this.plugin = plugin;
        this.dataDir = this.plugin.getDataFolder();
        dataDir.mkdir();

        this.playerZonesFile = new File(dataDir.getAbsolutePath().concat(File.separator).concat("playerZones.json"));
        playerZonesFile.createNewFile();
        if (playerZonesFile.length() == 0) { //writing empty List<PlayerZone> to JSON file so we can access it later
            Type playerZoneListType = new TypeToken<List<PlayerZone>>() {
            }.getType();
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

    public static ConfigReader getInstance(Zone2 plugin) throws IOException {
        if (instance == null) {
            instance = new ConfigReader(plugin);
        }
        return instance;
    }

    public void destroy() {
        instance = null;
    }

    public List<PlayerZone> getPlayerZoneList() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader playerZoneListReader = new JsonReader(new FileReader(this.playerZonesFile));

        Type playerZoneListType = new TypeToken<List<PlayerZone>>() {
        }.getType();
        return gson.fromJson(playerZoneListReader, playerZoneListType);
    }

    public List<ServerZone> getServerZoneList() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader serverZoneListReader = new JsonReader(new FileReader(this.serverZonesFile));

        Type serverZoneListType = new TypeToken<List<ServerZone>>() {
        }.getType();
        return gson.fromJson(serverZoneListReader, serverZoneListType);
    }
}

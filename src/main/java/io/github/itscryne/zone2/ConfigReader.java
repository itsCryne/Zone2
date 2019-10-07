package io.github.itscryne.zone2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConfigReader {
    private Zone2 plugin;
    private File dataDir;
    private File playerZonesFile;
    private File serverZonesFile;

    public ConfigReader(Zone2 plugin) throws IOException {

        this.plugin = plugin;
        this.dataDir = this.plugin.getDataFolder();

        this.playerZonesFile = new File(dataDir.getAbsolutePath() + "/playerZones.json");
        playerZonesFile.createNewFile();
        if (playerZonesFile.length() == 0) { //writing empty List<PlayerZone> to JSON file so we can access it later
            Type playerZoneListType = new TypeToken<List<PlayerZone>>() {}.getType();
            List<PlayerZone> playerZoneList = new ArrayList<>();

            Gson gson = new Gson();

            JsonWriter playerZoneListWriter = new JsonWriter(new FileWriter(this.playerZonesFile));
            gson.toJson(playerZoneList, playerZoneListType, playerZoneListWriter);
        }
    }

    public List<PlayerZone> getPlayerZoneList() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader playerZoneListReader = new JsonReader(new FileReader(this.playerZonesFile));

        Type playerZoneListType = new TypeToken<List<ServerZone>>() {}.getType();
        return gson.fromJson(playerZoneListReader, playerZoneListType);
    }

    public List<ServerZone> getServerZoneList() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader serverZoneListReader = new JsonReader(new FileReader(this.serverZonesFile));

        Type serverZoneListType = new TypeToken<List<ServerZone>>() {}.getType();
        return gson.fromJson(serverZoneListReader, serverZoneListType);
    }
}

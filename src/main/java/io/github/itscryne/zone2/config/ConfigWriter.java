package io.github.itscryne.zone2.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.ServerZone;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConfigWriter {
    private static ConfigWriter instance;

    private final String playerZonePath;
    private final String serverZonePath;
    private Zone2 plugin;
    private File dataDir;
    private File playerZonesFile;
    private File serverZonesFile;

    /**
     * ConfigWriter constructor - access via {@link #getInstance(Zone2)} method.
     * @param plugin the plugin
     * @throws IOException if it cant access the files et al
     */
    private ConfigWriter(Zone2 plugin) throws IOException {
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
    public static ConfigWriter getInstance(Zone2 plugin) throws IOException {
        if (instance == null) {
            instance = new ConfigWriter(plugin);
        }
        return instance;
    }

    /**
     * Serializes a PlayerZone
     * @param zone the PlayerZone to write
     * @throws IOException if it cant access the file et al
     */
    public void writePlayerZone(PlayerZone zone) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();

        JsonReader playerZoneListReader = new JsonReader(new FileReader(this.playerZonesFile));
        Type playerZoneListType = new TypeToken<List<PlayerZone>>() {
        }.getType();
        List<PlayerZone> playerZoneList = gson.fromJson(playerZoneListReader, playerZoneListType);
        playerZoneList.add(zone);

        Writer fw = new FileWriter(playerZonePath);
        try {
            gson.toJson(playerZoneList, fw);
        } catch (StackOverflowError e) {
            throw e;
        }
        fw.flush();
        fw.close();
    }

    /**
     * Serializes a ServerZone
     * @param zone the ServerZone to write
     * @throws IOException if it cant access the file et al
     */
    public void writeServerZone(ServerZone zone) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();

        JsonReader serverZoneListReader = new JsonReader(new FileReader(this.serverZonesFile));

        Type serverZoneListType = new TypeToken<List<ServerZone>>() {
        }.getType();
        List<ServerZone> serverZoneList = gson.fromJson(serverZoneListReader, serverZoneListType);
        serverZoneList.add(zone);

        Writer fw = new FileWriter(serverZonePath);
        gson.toJson(serverZoneList, fw);
        fw.flush();
        fw.close();
    }

    /**
     * Helper function
     * @param playerZoneList List to search in
     * @param id The id to search for
     * @return the index of the id
     */
    private int findPlayerZoneIndexById(List<PlayerZone> playerZoneList, int id) {
        for (PlayerZone p : playerZoneList) {
            if (p.getId() == id) {
                return playerZoneList.indexOf(p);
            }
        }
        return -1;
    }

    /**
     * Deletes a serialized PlayerZone
     * @param id ID of the PlayerZone to delete
     * @throws IOException if it cant access the file et al
     */
    public void deletePlayerZone(int id) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();

        JsonReader playerZoneListReader = new JsonReader(new FileReader(this.playerZonesFile));

        Type playerZoneListType = new TypeToken<List<PlayerZone>>() {
        }.getType();
        List<PlayerZone> playerZoneList = gson.fromJson(playerZoneListReader, playerZoneListType);

        int indexToDelete = findPlayerZoneIndexById(playerZoneList, id);
        if (indexToDelete == -1) {
            return;
        } else {
            playerZoneList.remove(indexToDelete);
        }

        Writer fw = new FileWriter(playerZonePath);
        gson.toJson(playerZoneList, fw);
        fw.flush();
        fw.close();
    }

    /**
     * Helper function
     * @param serverZoneList List to search in
     * @param id The id to search for
     * @return the index of the id
     */
    private int findServerZoneIndexById(List<ServerZone> serverZoneList, int id) {
        for (ServerZone p : serverZoneList) {
            if (p.getId() == id) {
                return serverZoneList.indexOf(p);
            }
        }
        return -1;
    }

    /**
     * Deletes a serialized ServerZone
     * @param id ID of the ServerZone to delete
     * @throws IOException if it cant access the file et al
     */
    public void deleteServerZone(int id) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();

        JsonReader serverZoneListReader = new JsonReader(new FileReader(this.serverZonesFile));

        Type serverZoneListType = new TypeToken<List<ServerZone>>() {
        }.getType();
        List<ServerZone> serverZoneList = gson.fromJson(serverZoneListReader, serverZoneListType);

        int indexToDelete = findServerZoneIndexById(serverZoneList, id);
        if (indexToDelete == -1) {
            return;
        } else {
            serverZoneList.remove(indexToDelete);
        }

        Writer fw = new FileWriter(serverZonePath);
        gson.toJson(serverZoneList, fw);
        fw.flush();
        fw.close();
    }
    /**
     * Destroys the ConfigWriter instance
     */
    public void destroy() {
        instance = null;
    }
}

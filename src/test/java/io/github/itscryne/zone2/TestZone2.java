package io.github.itscryne.zone2;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.spaces.PlayerZone;
import io.github.itscryne.zone2.spaces.ServerZone;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestZone2 {
    private ServerMock server;
    private Zone2 plugin;
    private PlayerMock cryne;

    @Before
    public void setUp() {
        this.server = MockBukkit.mock();
        this.plugin = (Zone2) MockBukkit.load(Zone2.class);
        this.cryne = this.server.addPlayer("Cryne");
        this.cryne.setOp(true);
    }

    @After
    public void tearDown() {
        MockBukkit.unload();
    }

    @Test
    public void testZoneCreation() throws IOException {
        this.server.execute("createzone", this.cryne, "10", "-10", "10", "-10");

        ConfigReader reader = ConfigReader.getInstance(this.plugin);
        List<PlayerZone> pzl = reader.getPlayerZoneList();

        GsonBuilder builder = new GsonBuilder(); 
        Gson gson = builder.create(); 

        String playerZoneString = gson.toJson(pzl.get(0));
        String pCorrect = "{\"name\":\"Cryne\",\"playerUuid\":\"b9dca703-ce21-3a6d-97ae-8ea2e1c0a12c\",\"perms\":[],\"priority\":1,\"id\":0,\"serL1\":{\"world\":\"world\",\"x\":10.0,\"y\":256.0,\"z\":10.0,\"pitch\":0.0,\"yaw\":0.0},\"serL2\":{\"world\":\"world\",\"x\":-10.0,\"y\":0.0,\"z\":-10.0,\"pitch\":0.0,\"yaw\":0.0}}";
        
        assert playerZoneString.equals(pCorrect);


        this.server.execute("createserverzone", this.cryne, "10", "-10", "255", "1", "10", "-10", "world", "1", "Cryne");

        List<ServerZone> szl = reader.getServerZoneList();

        String serverZoneString = gson.toJson(szl.get(0));
        String sCorrect = "{\"name\":\"Cryne\",\"priority\":1,\"id\":0,\"serL1\":{\"world\":\"world\",\"x\":10.0,\"y\":255.0,\"z\":10.0,\"pitch\":0.0,\"yaw\":0.0},\"serL2\":{\"world\":\"world\",\"x\":-10.0,\"y\":1.0,\"z\":-10.0,\"pitch\":0.0,\"yaw\":0.0}}";

        assert serverZoneString.equals(sCorrect);
    }
}

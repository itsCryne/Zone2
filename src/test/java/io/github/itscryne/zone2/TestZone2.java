package io.github.itscryne.zone2;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

import java.io.IOException;

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
        assert this.plugin.isEnabled() == true; //TODO add tests later
    }
}

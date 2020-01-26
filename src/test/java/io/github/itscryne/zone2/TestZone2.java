package io.github.itscryne.zone2;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestZone2 {
    private ServerMock server;
    private Zone2 plugin;

    @Before
    public void setUp()
    {
        server = MockBukkit.mock();
        plugin = (Zone2) MockBukkit.load(Zone2.class);
    }

    @After
    public void tearDown()
    {
        MockBukkit.unload();
    }

    @Test
    public void testZone2(){
        //PlayerMock player = server.addPlayer("Cryne");



    }
}

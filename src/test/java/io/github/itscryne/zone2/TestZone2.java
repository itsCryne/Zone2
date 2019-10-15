package io.github.itscryne.zone2;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.After;
import org.junit.Before;

public class test {
    private ServerMock server;
    private Zone2 plugin;

    @Before
    public void setUp()
    {
        server = MockBukkit.mock();
        plugin = (Zone2) MockBukkit.load(Zone2.class);
        boolean result = testit();
        System.out.println(result);
    }

    @After
    public void tearDown()
    {
        MockBukkit.unload();
    }

    public boolean testit(){
        PlayerMock player = server.addPlayer("Cryne");
        return player.getName().equals("Cryne");
    }
}

package io.github.itscryne.zone2.events;

import org.bukkit.event.Listener;

import io.github.itscryne.zone2.Zone2;

public class Zone2BuildTypeEvent implements Listener {
    private Zone2 plugin;
    public Zone2BuildTypeEvent(Zone2 plugin){
        this.plugin = plugin;
    }
}
package io.github.itscryne.zone2.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import io.github.itscryne.zone2.extensions.Zonecation;
import io.github.itscryne.zone2.extensions.Zoneler;
import io.github.itscryne.zone2.spaces.Zone;

public class Zone2EventListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) throws IOException {
        Zonecation to = new Zonecation(event.getTo());

        List<Zone> zoneList = new ArrayList<>();
        zoneList.addAll(to.getPlayerZones());
        zoneList.addAll(to.getServerZones());

        String message = "";

        if (zoneList.isEmpty()) {
            message = "Keine Zone";
        } else if (zoneList.size() == 1) {
            message = zoneList.get(0).getName();
        } else {
            for (Zone elem : zoneList) {
                message = message.concat(elem.getName().concat(", "));
            }
            message = message.substring(0, message.length() - 2);
        }

        new Zoneler(event.getPlayer()).sendXPMessage(message, false);
    }

}

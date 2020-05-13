package io.github.itscryne.zone2.events;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.extensions.ZLocation;
import io.github.itscryne.zone2.extensions.ZPlayer;
import io.github.itscryne.zone2.spaces.Zone;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zone2EventListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) throws IOException {
        ZLocation to = new ZLocation(event.getTo());

        List<Zone> zoneList = new ArrayList<>();
        zoneList.addAll(to.getPlayerZones());
        zoneList.addAll(to.getServerZones());

        String message = "";

        if (zoneList.isEmpty()) {
            message = Zone2.getPlugin().getConfig().getString("noZoneHere");
        } else if (zoneList.size() == 1) {
            message = zoneList.get(0).getName();
        } else {
            for (Zone elem : zoneList) {
                message = message.concat(elem.getName().concat(", "));
            }
            message = message.substring(0, message.length() - 2);
        }

        new ZPlayer(event.getPlayer()).sendXPMessage(message, false);
    }

}

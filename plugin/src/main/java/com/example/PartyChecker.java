package com.example;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.util.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class PartyChecker {

    // Values and a bunch of the code, based on:
    // https://github.com/QuestingPet/ToaMistakeTracker/blob/master/src/main/java/com/toamistaketracker/RaidState.java

    private int TOA_LOBBY_INSIDE = 14160;
    private int TOA_LOBBY_OUTSIDE = 13454;

    private static final int TOA_RAIDERS_VARC_START = 1099;
    private static final int MAX_RAIDERS = 8;

    public Map<String, Player> partyPlayerMap = new HashMap<>();


    public boolean checkRegion(Client client) {
        final int regionId = getCurrentRegionId(client);
        log.info("[TOA SUPPLY CHECKER] Entered region: " + regionId);

        if (regionId == TOA_LOBBY_INSIDE) {
            log.info("[TOA SUPPLY CHECKER] We there!");
            partyPlayerMap = tryLoadRaiders(client);
            return true;
        }
        return false;
    }

    public int getCurrentRegionId(Client client) {
        Player player = client.getLocalPlayer();
        if (player == null) return -1;
        return WorldPoint.fromLocalInstance(client, player.getLocalLocation()).getRegionID();
    }

    private Map<String, Player> tryLoadRaiders(Client client) {
        Set<String> raiderNamesShown = new HashSet<>(10);
        for (int i = 0; i < MAX_RAIDERS; i++) {
            String name = client.getVarcStrValue(TOA_RAIDERS_VARC_START + i);
            if (name != null && !name.isEmpty()) {
                raiderNamesShown.add(Text.sanitize(name));
            }
        }

        Map<String, Player> raiders = new HashMap<>(10);
        for (Player player : client.getPlayers()) {
            if (player != null &&
                    player.getName() != null &&
                    !raiders.containsKey(player.getName()) &&
                    raiderNamesShown.contains(player.getName())) {
                raiders.put(player.getName(), player);
            }
        }

        log.debug("[TOA SUPPLY CHECKER] Loaded raiderNames: {}", raiderNamesShown);
        log.debug("[TOA SUPPLY CHECKER] Loaded raiders: {}", raiders.keySet());

        return raiders;
    }
}

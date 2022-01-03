package com.mceon.eonranks.ranks;

import java.util.HashMap;
import java.util.List;

public enum Ranks {
    MEMBER,
    TRAVELER,
    EXPLORER,
    RANGER,
    SPACEMAN,
    ASTRONAUT,
    MYTHIC,
    GAMER;

    public static Ranks getNext(Ranks rank) {
        List<Ranks> rankList = List.of(Ranks.values());
        if (!rank.equals(Ranks.GAMER)) {
            return rankList.get(rankList.indexOf(rank) + 1);
        } else return Ranks.GAMER;
    }

    public static HashMap<Ranks, Integer> getRankupMap() {
        HashMap<Ranks, Integer> retMap = new HashMap<>();
        retMap.put(MEMBER, 2500);
        retMap.put(TRAVELER, 5000);
        retMap.put(EXPLORER, 10000);
        retMap.put(RANGER, 25000);
        retMap.put(SPACEMAN, 50000);
        retMap.put(ASTRONAUT, 100000);
        retMap.put(MYTHIC, 500000);
        return retMap;
    }
}

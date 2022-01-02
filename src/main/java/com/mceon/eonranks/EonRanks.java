package com.mceon.eonranks;

import com.mceon.eonranks.ranks.RankMenuInteraction;
import org.bukkit.plugin.java.JavaPlugin;

public final class EonRanks extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerRanks();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerRanks() {
        new RankMenuInteraction(this);
    }
}

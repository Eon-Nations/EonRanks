package com.mceon.eonranks;

import com.mceon.eonranks.ranks.RankMenuInteraction;
import com.mceon.eonranks.ranks.RankupFlow;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class EonRanks extends JavaPlugin {
    LuckPerms luckPermsAPI;
    Economy econ;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        luckPermsAPI = loadLuckPerms();
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
        registerRanks();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerRanks() {
        new RankMenuInteraction(this);
        new RankupFlow(this, econ, luckPermsAPI);
    }

    private LuckPerms loadLuckPerms() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            return provider.getProvider();
        } else return null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


}

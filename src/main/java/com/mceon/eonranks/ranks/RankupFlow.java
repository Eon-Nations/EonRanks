package com.mceon.eonranks.ranks;

import com.mceon.eonranks.EonRanks;
import com.mceon.eonranks.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class RankupFlow implements CommandExecutor, Listener {

    EonRanks plugin;
    Economy economy;
    LuckPerms luckPermsAPI;

    public RankupFlow(EonRanks plugin, Economy economy, LuckPerms luckPermsAPI) {
        this.plugin = plugin;
        this.economy = economy;
        this.luckPermsAPI = luckPermsAPI;
        plugin.getCommand("rankup").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            double balance = economy.getBalance(p);
            HashMap<Ranks, Integer> rankupMap = Ranks.getRankupMap();
            Ranks playerRank = getPlayerRank(p);

            if (rankupMap.get(playerRank) <= balance) {
                p.openInventory(getConfirmationInventory(playerRank));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
            } else {
                double difference = Utils.round(rankupMap.get(playerRank) - balance, 2);
                p.sendMessage(getPrefix().append(Component.text("You need $" + difference + " more before you can rankup")));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            }
        }
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().title().equals(Component.text("Rankup Menu").color(TextColor.color(0, 255, 0)))) {
            Player p = (Player) e.getWhoClicked();
            switch (e.getCurrentItem().getType()) {
                // Accepting to rankup
                case LIME_CONCRETE -> {
                    HashMap<Ranks, Integer> rankupMap = Ranks.getRankupMap();
                    double withdrawalAmount = rankupMap.get(getPlayerRank(p));
                    economy.withdrawPlayer(p, withdrawalAmount);
                    // Promotion step
                    String command = "lp user " + p.getName() + " promote ranks";
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    p.closeInventory();
                    Ranks nextRank = Ranks.getNext(getPlayerRank(p));
                    Bukkit.getServer().sendMessage(getPrefix()
                            .append(Component.text(p.getName() + " has ranked up to " +
                                    StringUtils.capitalize(nextRank.name().toLowerCase()))));
                }
                // Declining to rankup
                case RED_CONCRETE -> {
                    p.closeInventory();
                    p.sendMessage(getPrefix().append(Component.text("See you again soon!")));
                }
            }
            e.setCancelled(true);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
        }
    }

    private Inventory getConfirmationInventory(Ranks rank) {
        TextColor lightBlue = TextColor.color(0, 204, 204);
        Ranks nextRank = Ranks.getNext(rank);
        Inventory inventory = Bukkit.createInventory(null, 27,
                Component.text("Rankup Menu").color(TextColor.color(0, 255, 0)));

        Utils.createItem(inventory, Material.LIME_CONCRETE, 1, 12,
                Component.text("Confirm").color(lightBlue));
        Utils.createItem(inventory, Material.GREEN_STAINED_GLASS, 1, 14,
                Component.text("Rankup Info").color(lightBlue),
                Component.text("Current Rank: " + StringUtils.capitalize(rank.name().toLowerCase()))
                        .color(lightBlue),
                Component.text("Next Rank: " + StringUtils.capitalize(nextRank.name().toLowerCase()))
                        .color(lightBlue));
        Utils.createItem(inventory, Material.RED_CONCRETE, 1, 16,
                Component.text("Deny").color(TextColor.color(255, 0, 0)));
        Utils.makeDummySlots(inventory);
        return inventory;
    }

    private Ranks getPlayerRank(Player p) {
        User user = luckPermsAPI.getUserManager().getUser(p.getUniqueId());
        ImmutableContextSet contextSet = luckPermsAPI.getContextManager().getContext(user).orElseGet(luckPermsAPI.getContextManager()::getStaticContext);
        CachedMetaData cachedMetaData = user.getCachedData().getMetaData(QueryOptions.contextual(contextSet));
        String prefix = cachedMetaData.getPrefix();
        if (prefix.equals("default")) prefix = "member";
        return Ranks.valueOf(prefix.toUpperCase());
    }

    private Component getPrefix() {
        return Component.text("[").color(TextColor.color(128, 128, 128))
                .append(Component.text("Eon Ranks").color(TextColor.color(0, 128, 255))
                        .append(Component.text("] ").color(TextColor.color(128, 128, 128))));
    }
}

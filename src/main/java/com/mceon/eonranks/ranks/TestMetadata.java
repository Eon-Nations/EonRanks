package com.mceon.eonranks.ranks;

import com.mceon.eonranks.EonRanks;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TestMetadata implements Listener {

    EonRanks plugin;
    LuckPerms luckPermsAPI;

    public TestMetadata(EonRanks plugin, LuckPerms luckPermsAPI) {
        this.plugin = plugin;
        this.luckPermsAPI = luckPermsAPI;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        CachedMetaData metaData = luckPermsAPI.getPlayerAdapter(Player.class).getMetaData(e.getPlayer());
        e.getPlayer().sendMessage("Your level is " + metaData.getMetaValue("level", Integer::parseInt).orElse(-1));

        User user = luckPermsAPI.getUserManager().getUser(e.getPlayer().getUniqueId());
        MetaNode node = MetaNode.builder("level", Integer.toString(1)).build();
        user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("level")));
        user.data().add(node);

        CachedMetaData newMetaData = luckPermsAPI.getPlayerAdapter(Player.class).getMetaData(e.getPlayer());
        e.getPlayer().sendMessage("Your level is " + newMetaData.getMetaValue("level", Integer::parseInt).orElse(-1));
    }
}

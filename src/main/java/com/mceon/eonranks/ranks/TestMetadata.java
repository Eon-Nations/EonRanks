package com.mceon.eonranks.ranks;

import com.mceon.eonranks.EonRanks;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collection;

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
        Collection<Node> nodeCollection = user.data().toCollection();
        nodeCollection.forEach(node -> e.getPlayer().sendMessage("Node: " + node.getKey()));
    }
}

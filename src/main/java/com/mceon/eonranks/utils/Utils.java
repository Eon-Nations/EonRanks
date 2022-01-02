package com.mceon.eonranks.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {

    public static @NotNull Component chat(String s) {
        return Component.text(ChatColor.translateAlternateColorCodes('&', s));
    }

    public static void createItem(Inventory inv, Material material, int amount, int invSlot, Component displayName, Component... loreString) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        Collections.addAll(lore, loreString);

        meta.displayName(displayName);
        meta.lore(lore);
        item.setItemMeta(meta);
        inv.setItem(invSlot - 1, item);
    }

    public static void makeDummySlots(Inventory inv) {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(""));
        item.setItemMeta(meta);

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, item);
            }
        }
    }

}

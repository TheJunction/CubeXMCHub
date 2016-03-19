package net.cubexmc.CubeXMCHub;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class Pvp {

    public static List<Player> pvpers = new ArrayList<>();

    public static List<Material> clearItems = new ArrayList<>();

    static {
        clearItems.add(Material.INK_SACK);
        clearItems.add(Material.ENDER_CHEST);
        clearItems.add(Material.BOOK);
    }

    public static boolean isPvp(Player p) {
        return pvpers.contains(p);
    }

    public static void loadInventory(Player p) {
        for (Material m : clearItems) {
            p.getInventory().remove(m);
        }
        if (p.hasPermission("cubexmchub.toggle")) {
            ItemStack stack = new MaterialData(Material.INK_SACK).toItemStack();
            stack.setDurability((short) 10);
            stack.setAmount(1);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(ChatColor.BLUE + "Punching Enabled");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.RED + "Right click to disable punching!");
            meta.setLore(lore);
            stack.setItemMeta(meta);
            p.getInventory().addItem(stack);
        }
        ItemStack stack = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Skulls Collected");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Right click a skull to collect it!");
        meta.setLore(lore);
        stack.setItemMeta(meta);
        p.getInventory().addItem(stack);
        p.setWalkSpeed(0.7f);
    }
}

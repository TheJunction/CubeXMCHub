package net.cubexmc.CubeXMCHub;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Pvp {

    private static final int INV_SIZE = 34;
    static List<Player> pvpers = new ArrayList<>();

    static boolean isPvp(Player p) {
        return pvpers.contains(p);
    }

    static void loadInventory(Player p) {
        File f = new File(CubeXMCHub.plugin.getDataFolder() + File.separator + "playerdata" + File.separator + p.getUniqueId().toString() + ".yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        List<?> itemList = con.getList("inv.items");
        List<?> armorList = con.getList("inv.armor");
        if (itemList != null && !itemList.isEmpty()) {
            p.getInventory().setContents(itemList.toArray(new ItemStack[itemList.size()]));
        }
        if (armorList != null && !armorList.isEmpty()) {
            p.getInventory().setArmorContents(armorList.toArray(new ItemStack[armorList.size()]));
        }

        p.getInventory().remove(Material.INK_SACK);
        p.getInventory().remove(Material.BOOK);
        p.getInventory().remove(Material.ENDER_CHEST);

        while (p.getInventory().getItem(0) != null && p.getInventory().getItem(1) != null && p.getInventory().getItem(2) != null) {
            for (int i = 0; i < INV_SIZE; i++) {
                int j = INV_SIZE - i;
                p.getInventory().setItem(j + 1, p.getInventory().getItem(j));
                p.getInventory().clear(j);
            }
        }

        ItemStack stack = new MaterialData(Material.INK_SACK).toItemStack();
        stack.setDurability((short) 10);
        stack.setAmount(1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Punching Enabled");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "Right click to disable punching!");
        meta.setLore(lore);
        stack.setItemMeta(meta);
        p.getInventory().setItem(0, stack);

        stack = new ItemStack(Material.BOOK, 1);
        meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Skulls Collected");
        lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Right click a skull to collect it!");
        meta.setLore(lore);
        stack.setItemMeta(meta);
        p.getInventory().setItem(1, stack);

        stack = new ItemStack(Material.ENDER_CHEST);
        meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "Particle Menu!");
        stack.setItemMeta(meta);
        p.getInventory().setItem(2, stack);

        p.setWalkSpeed(0.7f);
        con.set("inv", null);
        try {
            con.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

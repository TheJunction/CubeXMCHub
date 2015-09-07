/*
 * Copyright (c) 2015 CubeXMC Network
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.CubeXMC.Hub;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 5/23/2015.
 *
 * @author David
 */
@SuppressWarnings("deprecation")
class Toggle implements Listener {
    private final MaterialData match = new MaterialData(Material.INK_SACK, (byte) 10);
    private final MaterialData match2 = new MaterialData(Material.INK_SACK, (byte) 8);

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("cubexmchub.toggle")) {
            ItemStack stack = match.toItemStack();
            stack.setAmount(1);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(ChatColor.BLUE + "Punching Enabled");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.RED + "Right click to disable punching!");
            setLore(lore, meta, e, stack);
        }
        ItemStack stack = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Skulls Collected");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Right click a skull to collect it!");
        setLore(lore, meta, e, stack);
    }

    private void setLore(List<String> lore, ItemMeta meta, PlayerJoinEvent e, ItemStack stack) {
        meta.setLore(lore);
        stack.setItemMeta(meta);
        Player p = e.getPlayer();
        p.getInventory().setItem(p.getInventory().firstEmpty(), stack);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (Main.enabling.contains(e.getPlayer().getName())) {
            Main.enabling.remove(e.getPlayer().getName());
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Player p = e.getPlayer();
            ItemStack stack = p.getItemInHand();
            ItemMeta meta = stack.getItemMeta();
            if (stack.getData().equals(match) && !Main.enabling.contains(p.getName())) {
                Main.enabling.add(p.getName());
                meta.setDisplayName(ChatColor.RED + "Punching Disabled");
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.BLUE + "Right click to enable punching!");
                meta.setLore(lore);
                stack.setDurability((short) 8);
                p.sendMessage(String.valueOf(Main.title) + ChatColor.RED + " Punching is now disabled!");
            } else if (stack.getData().equals(match2) && Main.enabling.contains(p.getName())) {
                Main.enabling.remove(p.getName());
                meta.setDisplayName(ChatColor.BLUE + "Punching Enabled");
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.RED + "Right click to disable punching!");
                meta.setLore(lore);
                stack.setDurability((short) 10);
                p.sendMessage(String.valueOf(Main.title) + ChatColor.GREEN + " Punching is now enabled!");
            }
            stack.setItemMeta(meta);
        }
    }
}

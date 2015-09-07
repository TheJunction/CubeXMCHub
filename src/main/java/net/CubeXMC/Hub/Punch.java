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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

class Punch implements Listener {
    @EventHandler
    public void onClick(final EntityDamageByEntityEvent e) {
        final Player p = (Player) e.getDamager();
        final Player p2 = (Player) e.getEntity();
        if (p != null && p2 != null && p.hasPermission("cubexmchub.superpunch") && !Main.enabling.contains(p.getName()) && p.isSneaking()) {
            e.setCancelled(true);
            if (!Main.enabling.contains(p2.getName()) && (!Main.cooldown.contains(p.getName()) || p.hasPermission("cubexmchub.bypass"))) {
                p2.setVelocity(new Vector(0, 50, 0));
                punch(p, p2);
            } else {
                p.sendMessage(Main.title + ChatColor.RED + " You are either still on cooldown, or that person doesn't want to be punched!");
            }
        } else if (p != null && p2 != null && p.hasPermission("cubexmchub.punch")) {
            e.setCancelled(true);
            if (Main.canPunch(p, p2)) {
                p2.setVelocity(p.getLocation().getDirection().multiply(5));
                p2.setVelocity(new Vector(p2.getVelocity().getX(), 1.0, p2.getVelocity().getZ()));
                punch(p, p2);
            } else {
                p.sendMessage(Main.title + ChatColor.RED + " You are either still on cooldown, or that person doesn't want to be punched!");
            }
        }
    }

    private void punch(Player p, Player p2) {
        p.sendMessage(Main.title + ChatColor.GREEN + " FALCON..... PUNCH!");
        p2.sendMessage(Main.title + ChatColor.GOLD + " " + p.getName() + " has punched you!");
        Main.cooldown.add(p.getName());
        Main.coolDown(p.getName());
    }
}

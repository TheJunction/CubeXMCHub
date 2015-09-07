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

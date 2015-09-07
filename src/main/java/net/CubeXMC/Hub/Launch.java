package net.CubeXMC.Hub;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

class Launch implements Listener {
    @EventHandler
    public void onClick(final PlayerInteractEntityEvent e) {
        final Player p = e.getPlayer();
        final Player p2 = (Player) e.getRightClicked();
        if (p2 != null && p.hasPermission("cubexmchub.punch")) {
            if (Main.canPunch(p, p2)) {
                final Firework fw = (Firework) p2.getWorld().spawnEntity(p2.getLocation(), EntityType.FIREWORK);
                final FireworkMeta fwm = fw.getFireworkMeta();
                final Random r = new Random();
                final int rt = r.nextInt(4) + 1;
                FireworkEffect.Type type = FireworkEffect.Type.BALL;
                if (rt == 1) {
                    type = FireworkEffect.Type.BALL;
                }
                if (rt == 2) {
                    type = FireworkEffect.Type.BALL_LARGE;
                }
                if (rt == 3) {
                    type = FireworkEffect.Type.BURST;
                }
                if (rt == 4) {
                    type = FireworkEffect.Type.CREEPER;
                }
                if (rt == 5) {
                    type = FireworkEffect.Type.STAR;
                }
                final int r1i = r.nextInt(17);
                final int r2i = r.nextInt(17);
                final Color c1 = this.getColor(r1i);
                final Color c2 = this.getColor(r2i);
                final FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
                fwm.addEffect(effect);
                final int rp = r.nextInt(2) + 1;
                fwm.setPower(rp);
                fw.setFireworkMeta(fwm);
                fw.setPassenger(p2);
                p.sendMessage(Main.title + ChatColor.GREEN + " Pew Pew!");
                p2.sendMessage(Main.title + ChatColor.GOLD + " " + p.getName() + " has launched you!");
                Main.cooldown.add(p.getName());
                Main.coolDown(p.getName());
            } else {
                p.sendMessage(Main.title + ChatColor.RED + " You are either still on cooldown, or that person doesn't want to be punched!");
            }
        }
    }

    private Color getColor(final int i) {
        Color c = null;
        if (i == 1) {
            c = Color.AQUA;
        }
        if (i == 2) {
            c = Color.BLACK;
        }
        if (i == 3) {
            c = Color.BLUE;
        }
        if (i == 4) {
            c = Color.FUCHSIA;
        }
        if (i == 5) {
            c = Color.GRAY;
        }
        if (i == 6) {
            c = Color.GREEN;
        }
        if (i == 7) {
            c = Color.LIME;
        }
        if (i == 8) {
            c = Color.MAROON;
        }
        if (i == 9) {
            c = Color.NAVY;
        }
        if (i == 10) {
            c = Color.OLIVE;
        }
        if (i == 11) {
            c = Color.ORANGE;
        }
        if (i == 12) {
            c = Color.PURPLE;
        }
        if (i == 13) {
            c = Color.RED;
        }
        if (i == 14) {
            c = Color.SILVER;
        }
        if (i == 15) {
            c = Color.TEAL;
        }
        if (i == 16) {
            c = Color.WHITE;
        }
        if (i == 17) {
            c = Color.YELLOW;
        }
        return c;
    }
}

package net.CubeXMC.Hub;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Pvp implements Listener {
    public static List<Player> pvpers = new ArrayList<>();

    public static boolean isPvp(Player p) {
        return pvpers.contains(p);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setWalkSpeed(0.7f);
        Main.loadInventory(event.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        MaterialData standingOn = event.getTo().getBlock().getRelative(BlockFace.DOWN).getState().getData();
        if (standingOn instanceof Wool && ((Wool) standingOn).getColor() == DyeColor.GRAY) {
            p.setWalkSpeed(0.7f);
        } else {
            p.setWalkSpeed(0.2f);
        }
        if (event.getTo().getBlockY() <= 60 && !pvpers.contains(p)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 255));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 255));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 60, 255));
            for (int i = 0; i < 27; i++) {
                p.getEnderChest().setItem(i, p.getInventory().getItem(i));
            }
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            ItemStack[] inv = Main.invs.get(p.getUniqueId());
            ItemStack[] armor = Main.armors.get(p.getUniqueId());
            if (inv != null) {
                p.getInventory().setContents(inv);
            }
            if (armor != null) {
                p.getInventory().setArmorContents(armor);
            }
            pvpers.add(p);
        } else if (event.getTo().getBlockY() > 60 && pvpers.contains(p)) {
            Main.invs.put(p.getUniqueId(), p.getInventory().getContents());
            Main.armors.put(p.getUniqueId(), p.getInventory().getArmorContents());
            p.getInventory().setArmorContents(null);
            p.getInventory().setContents(p.getEnderChest().getContents());
            pvpers.remove(p);
        }
        if (event.getTo().getBlockY() <= 60 && p.isFlying()) {
            p.setFlying(false);
        }
    }

    @EventHandler
    public void shift(PlayerToggleSneakEvent event) {
        Location playerLoc = event.getPlayer().getLocation();
        if (event.isSneaking() && playerLoc.getBlock().getRelative(BlockFace.DOWN).getType() == Material.IRON_TRAPDOOR) {
            event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), Math.floor(playerLoc.getBlockX()) + 0.5, 63, Math.floor(playerLoc.getBlockZ()) + 0.5));
        }
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        event.getPlayer().setWalkSpeed(0.7f);
        Main.invs.put(p.getUniqueId(), p.getInventory().getContents());
        Main.armors.put(p.getUniqueId(), p.getInventory().getArmorContents());
        p.getInventory().setArmorContents(null);
        p.getInventory().setContents(p.getEnderChest().getContents());
        pvpers.remove(p);
    }

    @EventHandler
    public void damage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player p = (Player) event.getEntity();
            if (!isPvp(p) && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void attack(EntityDamageByEntityEvent event) {
        final Player p = (Player) event.getDamager();
        final Player p2 = (Player) event.getEntity();
        if (p != null && p2 != null) {
            if (!isPvp(p)) {
                p.sendMessage(ChatColor.RED + "You cannot attack while in a no-pvp zone!");
                event.setCancelled(true);
            } else if (!isPvp(p2)) {
                p.sendMessage(ChatColor.RED + "You cannot attack someone in a no-pvp zone!");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (pvpers.contains(p)) {
            Main.invs.put(p.getUniqueId(), p.getInventory().getContents());
            Main.armors.put(p.getUniqueId(), p.getInventory().getArmorContents());
            pvpers.remove(p);
        }
        p.getInventory().setArmorContents(null);
        p.getInventory().clear();
        Main.saveInventory(p);
    }
}

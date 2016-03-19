package net.cubexmc.CubeXMCHub;

import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

class Listeners implements Listener {

    private static Dye match;
    private static Dye match2;

    static {
        match = new Dye();
        match.setColor(DyeColor.LIME);
        match2 = new Dye();
        match2.setColor(DyeColor.GRAY);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Pvp.loadInventory(e.getPlayer());
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack stack = p.getInventory().getItemInMainHand();
            ItemMeta meta = stack.getItemMeta();
            if (!noFlyZone(p) && stack.getType() == Material.BLAZE_POWDER) {
                p.setVelocity(p.getLocation().getDirection().multiply(5));
            } else if (p.hasPermission("cubexmchub.toggle")) {
                if (stack.getData().equals(match) && !CubeXMCHub.enabling.contains(p.getName())) {
                    CubeXMCHub.enabling.add(p.getName());
                    meta.setDisplayName(ChatColor.RED + "Punching Disabled");
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.BLUE + "Right click to enable punching!");
                    meta.setLore(lore);
                    stack.setDurability((short) 8);
                    p.sendMessage(String.valueOf(CubeXMCHub.title) + ChatColor.RED + " Punching is now disabled!");
                } else if (stack.getData().equals(match2) && CubeXMCHub.enabling.contains(p.getName())) {
                    CubeXMCHub.enabling.remove(p.getName());
                    meta.setDisplayName(ChatColor.BLUE + "Punching Enabled");
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + "Right click to disable punching!");
                    meta.setLore(lore);
                    stack.setDurability((short) 10);
                    p.sendMessage(String.valueOf(CubeXMCHub.title) + ChatColor.GREEN + " Punching is now enabled!");
                }
                stack.setItemMeta(meta);
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.SKULL) {
                Skull b = (Skull) e.getClickedBlock().getState();
                String s = b.getOwner() == null ? "" : b.getOwner();

                HeadHunt hh = HeadHuntUtil.getByUUID(p.getUniqueId());

                IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.GOLD + "Collected Skull!\"}");
                IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.GREEN + s + "\"}");

                PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
                PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
                PacketPlayOutTitle length = new PacketPlayOutTitle(5, 20, 5);


                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subTitle);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);

                if (hh != null && hh.collectSkull(s)) {
                    p.sendMessage(CubeXMCHub.title + ChatColor.GOLD + " You have collected " + ChatColor.GREEN + s + ChatColor.GOLD + "'s Head!");
                    switch (hh.collectedSkulls().size()) {
                        case 5:
                            HeadHunt.achMsg(p, "Bounty Hunter!", hh);
                            break;
                        case 10:
                            HeadHunt.achMsg(p, "Head Snatcher!", hh);
                            break;
                        case 20:
                            HeadHunt.achMsg(p, "The Collector!", hh);
                            break;
                        case 30:
                            HeadHunt.achMsg(p, "Heads Much?", hh);
                            break;
                        case 40:
                            HeadHunt.achMsg(p, "Heads! Heads! Heads!", hh);
                            break;
                        case 50:
                            HeadHunt.achMsg(p, "Getting Head!", hh);
                            break;
                        case 60:
                            HeadHunt.achMsg(p, "Still Going?", hh);
                            break;
                        default:
                            break;
                    }
                    HeadHunt.hBio(s, p);
                    if (!hh.getAchievements().contains("OwntheOwners") && hh.collectedSkulls().contains("Cux") && hh.collectedSkulls().contains("SandwichOverdose")) {
                        HeadHunt.achMsg(p, "Own the Owners!", hh);
                    }
                    if (!hh.getAchievements().contains("HeadsofAdmins") && hh.collectedSkulls().contains("Blitzkim2")) {
                        HeadHunt.achMsg(p, "Heads of Admins!", hh);
                    }

                } else {
                    p.sendMessage(CubeXMCHub.title + ChatColor.RED + " You have already collected that player!");
                }
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.getInventory().getItemInMainHand().getType() == Material.BOOK) {

                p.openInventory(HeadHunt.open());

            }
        }

    }

    @EventHandler
    public void interact(InventoryClickEvent e) {

        if (HeadHunt.getTest2() != null && Objects.equals(e.getInventory().getName(), HeadHunt.getTest2().getName())) {

            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains("achievements!")) {
                e.getWhoClicked().openInventory(HeadHunt.openAch((Player) e.getWhoClicked()));
            }

            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Heads!")) {
                e.getWhoClicked().openInventory(HeadHunt.openSkulls((Player) e.getWhoClicked()));
            }
            e.setCancelled(true);
        }

        if (HeadHunt.getTest() != null && Objects.equals(e.getInventory().getName(), HeadHunt.getTest().getName())) {
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {
                String hNameStrip = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
                CubeXMCHub.logger.info(hNameStrip);
                if (CubeXMCHub.bioHeads.contains(hNameStrip)) {
                    HeadHunt.hBio(hNameStrip, (Player) e.getWhoClicked());
                }
            }
            e.setCancelled(true);
        }

        if (HeadHunt.getTest3() != null && Objects.equals(e.getInventory().getName(), HeadHunt.getTest3().getName())) {
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Head Hunt")) {
                e.getWhoClicked().openInventory(HeadHunt.hhAch((Player) e.getWhoClicked()));
            }
            e.setCancelled(true);
        }

        if (HeadHunt.getHhAch() != null && Objects.equals(e.getInventory().getName(), HeadHunt.getHhAch().getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(final PlayerInteractEntityEvent e) {
        final Player p = e.getPlayer();
        final Player p2 = (Player) e.getRightClicked();
        if (p2 != null && p.hasPermission("cubexmchub.punch") && !Pvp.isPvp(p) && !Pvp.isPvp(p2)) {
            if (CubeXMCHub.canPunch(p, p2)) {
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
                p.sendMessage(CubeXMCHub.title + ChatColor.GREEN + " Pew Pew!");
                p2.sendMessage(CubeXMCHub.title + ChatColor.GOLD + " " + p.getName() + " has launched you!");
                CubeXMCHub.cooldown.add(p.getName());
                CubeXMCHub.coolDown(p.getName());
            } else {
                p.sendMessage(CubeXMCHub.title + ChatColor.RED + " You are either still on cooldown, or that person doesn't want to be punched!");
            }
        }
    }

    @EventHandler
    public void onClick(final EntityDamageByEntityEvent e) {
        final Player p = (Player) e.getDamager();
        final Player p2 = (Player) e.getEntity();
        if (p != null && p2 != null && !Pvp.isPvp(p) && !Pvp.isPvp(p2)) {
            if (p.hasPermission("cubexmchub.superpunch") && !CubeXMCHub.enabling.contains(p.getName()) && p.isSneaking()) {
                e.setCancelled(true);
                if (!CubeXMCHub.enabling.contains(p2.getName()) && (!CubeXMCHub.cooldown.contains(p.getName()) || p.hasPermission("cubexmchub.bypass"))) {
                    p2.setVelocity(new Vector(0, 50, 0));
                    punch(p, p2);
                } else {
                    p.sendMessage(CubeXMCHub.title + ChatColor.RED + " You are either still on cooldown, or that person doesn't want to be punched!");
                }
            } else if (p.hasPermission("cubexmchub.punch")) {
                e.setCancelled(true);
                if (CubeXMCHub.canPunch(p, p2)) {
                    p2.setVelocity(p.getLocation().getDirection().multiply(5));
                    p2.setVelocity(new Vector(p2.getVelocity().getX(), 1.0, p2.getVelocity().getZ()));
                    punch(p, p2);
                } else {
                    p.sendMessage(CubeXMCHub.title + ChatColor.RED + " You are either still on cooldown, or that person doesn't want to be punched!");
                }
            }
        }
        if (p != null && p2 != null && p.getInventory().getItemInMainHand().getType() != Material.STICK) {
            if (!Pvp.isPvp(p)) {
                p.sendMessage(net.md_5.bungee.api.ChatColor.RED + "You cannot attack while in a no-pvp zone!");
                e.setCancelled(true);
            } else if (!Pvp.isPvp(p2)) {
                p.sendMessage(net.md_5.bungee.api.ChatColor.RED + "You cannot attack someone in a no-pvp zone!");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (CubeXMCHub.enabling.contains(e.getPlayer().getName())) {
            CubeXMCHub.enabling.remove(e.getPlayer().getName());
        }
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
        if (event.getTo().getBlockY() <= 60 && !Pvp.pvpers.contains(p)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 255));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 255));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 60, 255));
            Pvp.pvpers.add(p);
        } else if (event.getTo().getBlockY() > 60 && Pvp.pvpers.contains(p)) {
            Pvp.pvpers.remove(p);
        }
        if (noFlyZone(p)) {
            p.setFlying(false);
            p.setAllowFlight(false);
            p.sendMessage(net.md_5.bungee.api.ChatColor.RED + "You can't fly in this area!");
        }
    }

    /*@EventHandler
    public void onPlayerViolation(PlayerViolationEvent event) {
        if (event.getHackType().equals(HackType.SPEED)) {
            MaterialData standingOn = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getState().getData();
            if (standingOn instanceof Wool && ((Wool) standingOn).getColor() == DyeColor.GRAY) {
                event.setCancelled(true);
            }
        }
    }*/

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
        Pvp.pvpers.remove(p);
    }

    @EventHandler
    public void damage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player p = (Player) event.getEntity();
            if (!Pvp.isPvp(p) && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                event.setCancelled(true);
            }
        }
    }

    private void punch(Player p, Player p2) {
        p.sendMessage(CubeXMCHub.title + ChatColor.GREEN + " FALCON..... PUNCH!");
        p2.sendMessage(CubeXMCHub.title + ChatColor.GOLD + " " + p.getName() + " has punched you!");
        CubeXMCHub.cooldown.add(p.getName());
        CubeXMCHub.coolDown(p.getName());
    }

    private Color getColor(final int i) {
        Map<Integer, Color> colorMap = new HashMap<>();
        colorMap.put(1, Color.AQUA);
        colorMap.put(2, Color.BLACK);
        colorMap.put(3, Color.BLUE);
        colorMap.put(4, Color.FUCHSIA);
        colorMap.put(5, Color.GRAY);
        colorMap.put(6, Color.GREEN);
        colorMap.put(7, Color.LIME);
        colorMap.put(8, Color.MAROON);
        colorMap.put(9, Color.NAVY);
        colorMap.put(10, Color.OLIVE);
        colorMap.put(11, Color.ORANGE);
        colorMap.put(12, Color.PURPLE);
        colorMap.put(13, Color.RED);
        colorMap.put(14, Color.SILVER);
        colorMap.put(15, Color.TEAL);
        colorMap.put(16, Color.WHITE);
        colorMap.put(17, Color.YELLOW);
        return colorMap.get(i);
    }

    private boolean noFlyZone(Player p) {
        return !p.isOp() && (p.isFlying() || p.getAllowFlight()) && (p.getLocation().getBlockY() <= 60 || (7 <= p.getLocation().getBlockX() && p.getLocation().getBlockX() <= 36 && 65 <= p.getLocation().getBlockY() && p.getLocation().getBlockY() <= 131 && 47 <= p.getLocation().getBlockZ() && p.getLocation().getBlockZ() <= 65));
    }
}
package net.CubeXMC.Hub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static final Logger logger = Logger.getLogger("Minecraft");
    public static final List<String> bioHeads = new ArrayList<>();
    public static String title;
    public static List<String> enabling;
    public static List<String> cooldown;
    public static HashMap<String, HeadHunt> hhunt;

    static {
        Main.title = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "CubeXMC" + ChatColor.DARK_GRAY + "]";
        Main.enabling = new ArrayList<>();
        Main.cooldown = new ArrayList<>();
        Main.hhunt = new HashMap<>();
    }

    public static boolean canPunch(Player p, Player p2) {
        return (!Main.enabling.contains(p2.getName()) && !Main.enabling.contains(p.getName()) && (!Main.cooldown.contains(p.getName()) || p.hasPermission("cubexmchub.bypass")));
    }

    public static void coolDown(final String s) {
        Bukkit.getServer().getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("CubeXMCHub"), new Runnable() {
            @Override
            public void run() {
                Main.cooldown.remove(s);
            }
        }, 200L);
    }

    public void onEnable() {

        final PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new Launch(), this);
        pm.registerEvents(new Punch(), this);
        pm.registerEvents(new Toggle(), this);
        pm.registerEvents(new HeadHuntListener(), this);

        File fdir = new File(getDataFolder() + "/playerdata/");
        fdir.mkdir();
        File[] aof;
        int j = 0;
        aof = fdir.listFiles();
        if (aof != null) {
            j = (aof).length;
        }
        for (int i = 0; i < j; i++) {
            File f = aof[i];
            String name = f.getName().replace(".yml", "");
            HeadHuntUtil.load(this, name);
        }

        bioHeads.add("PantherMan594");
        bioHeads.add("Vek");
        bioHeads.add("SandwichOverdose");
        bioHeads.add("Blitzkim2");
        bioHeads.add("Major_Dork");
        bioHeads.add("HYPExMon5ter");
        bioHeads.add("Jo_Dan");
        bioHeads.add("ShatteredMines2");
        bioHeads.add("terturl");
    }

    public void onDisable() {
        HeadHuntUtil.save(this);
    }

    public boolean onCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (cs instanceof Player) {
            final Player p = (Player) cs;
            if (p.hasPermission("cubexmchub.toggle") && label.equalsIgnoreCase("cubex")) {
                if (args[0].equalsIgnoreCase("enable")) {
                    if (Main.enabling.contains(p.getName())) {
                        Main.enabling.remove(p.getName());
                    }
                    p.sendMessage(String.valueOf(Main.title) + ChatColor.GREEN + " Punching is now enabled!");
                }
                if (args[0].equalsIgnoreCase("disable")) {
                    if (!Main.enabling.contains(p.getName())) {
                        Main.enabling.add(p.getName());
                    }
                    p.sendMessage(String.valueOf(Main.title) + ChatColor.RED + " Punching is now disabled!");
                }
            }
            if (p.hasPermission("cubexmchub.hhadmin") && label.equalsIgnoreCase("cubex")) {
                if (args[0].equalsIgnoreCase("statr")) {
                    if (args[1] != null) {
                        HeadHunt bh = HeadHuntUtil.getByName(args[1]);
                        if (bh != null) {
                            bh.collectedSkulls().clear();
                            bh.getAchievements().clear();
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("hhget")) {

                    Block b = p.getLocation().getBlock();

                    ItemStack i = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                    SkullMeta meta = (SkullMeta) i.getItemMeta();
                    meta.setOwner(args[1]);
                    i.setItemMeta(meta);

                    b.setType(i.getType());
                    p.getInventory().addItem(i);

                }
            }
        } else if (args[0].equalsIgnoreCase("clearconsole")) {
            for (int num = 0; num < 100; num++) {
                logger.info("");
            }
        }
        return false;
    }
}

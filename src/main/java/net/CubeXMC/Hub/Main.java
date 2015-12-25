package net.CubeXMC.Hub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static final Logger logger = Logger.getLogger("Minecraft");
    public static final List<String> bioHeads = new ArrayList<>();
    public static Plugin plugin;
    public static String title;
    public static List<String> enabling;
    public static List<String> cooldown;
    public static HashMap<UUID, HeadHunt> hhunt;
    public static HashMap<UUID, ItemStack[]> invs = new HashMap<>();
    public static HashMap<UUID, ItemStack[]> armors = new HashMap<>();

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

    public static void loadInventory(Player player) {
        File f = new File(plugin.getDataFolder() + File.separator + "playerdata" + File.separator + player.getUniqueId().toString() + ".yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        List<?> itemList = con.getList("inv.items");
        List<?> armorList = con.getList("inv.armor");
        if (itemList != null && !itemList.isEmpty()) {
            ItemStack[] inv = itemList.toArray(new ItemStack[itemList.size()]);
            invs.put(player.getUniqueId(), inv);
        }
        if (armorList != null && !armorList.isEmpty()) {
            ItemStack[] armor = armorList.toArray(new ItemStack[armorList.size()]);
            armors.put(player.getUniqueId(), armor);
        }
        try {
            con.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        update(player);
    }

    public static void saveInventory(Player p) {
        File f = new File(plugin.getDataFolder() + File.separator + "playerdata" + File.separator + p.getUniqueId().toString() + ".yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        ItemStack[] inv = invs.get(p.getUniqueId());
        ItemStack[] armor = armors.get(p.getUniqueId());
        if (inv != null && inv.length != 0) {
            con.set("inv.items", inv);
        }
        if (armor != null && armor.length != 0) {
            con.set("inv.armor", armor);
        }
        try {
            con.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        update(p);
    }

    public static void update(Player player) {
        File f = new File(plugin.getDataFolder() + File.separator + "playerdata" + File.separator + player.getUniqueId().toString() + ".yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        try {
            con.load(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        plugin = this;

        final PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new Launch(), this);
        pm.registerEvents(new Punch(), this);
        pm.registerEvents(new Toggle(), this);
        pm.registerEvents(new HeadHuntListener(), this);
        pm.registerEvents(new Pvp(), this);

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
            String uuid = f.getName().replace(".yml", "");
            HeadHuntUtil.load(this, UUID.fromString(uuid));
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
        for (Player p : getServer().getOnlinePlayers()) {
            Main.loadInventory(p);
        }
    }

    public void onDisable() {
        for (Player p : getServer().getOnlinePlayers()) {
            Main.saveInventory(p);
        }
        HeadHuntUtil.save(this);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (Pvp.pvpers.contains(p)) {
                Main.invs.put(p.getUniqueId(), p.getInventory().getContents());
                Main.armors.put(p.getUniqueId(), p.getInventory().getArmorContents());
                Pvp.pvpers.remove(p);
            }
            p.getInventory().setArmorContents(null);
            p.getInventory().clear();
            Main.saveInventory(p);
        }
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
                        HeadHunt bh = HeadHuntUtil.getByUUID(UUID.fromString(args[1]));
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

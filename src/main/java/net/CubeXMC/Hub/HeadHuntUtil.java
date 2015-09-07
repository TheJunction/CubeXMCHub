package net.CubeXMC.Hub;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class HeadHuntUtil {

    public static HeadHunt getByName(String name) {

        if (Main.hhunt.get(name) == null) {
            HeadHunt bh = new HeadHunt(name);
            Main.hhunt.put(name, bh);
            return bh;
        }
        if (Main.hhunt.get(name) == null) {
            return null;
        }

        return Main.hhunt.get(name);

    }

    public static void save(Plugin p) {

        for (HeadHunt bh : Main.hhunt.values()) {
            File f = new File(p.getDataFolder() + File.separator + "playerdata" + File.separator + bh.getName() + ".yml");
            if (f.exists()) {
                f.delete();
            }
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(f);
            ArrayList<String> collectedSkulls = bh.collectedSkulls();
            ArrayList<String> gotAch = bh.getAchievements();

            config.set("Collected-Skulls", collectedSkulls);
            config.set("Achievements", gotAch);

            try {
                config.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @SuppressWarnings("unchecked")
    public static void load(Plugin p, String name) {
        File f = new File(p.getDataFolder() + File.separator + "playerdata" + File.separator + name + ".yml");
        if (f.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(f);
            ArrayList<String> collectedSkulls = (ArrayList<String>) config.get("Collected-Skulls");
            ArrayList<String> Achievements = (ArrayList<String>) config.get("Achievements");

            HeadHunt hh = new HeadHunt(name);
            if (collectedSkulls != null) {
                for (String cs : collectedSkulls) {
                    hh.collectSkull(cs);
                }
            }

            if (Achievements != null) {
                for (String a : Achievements) {
                    hh.addAche(a);
                }
            }
            Main.hhunt.put(name, hh);
        }
    }

}
package net.cubexmc.CubeXMCHub;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

class HeadHuntUtil {

    static HeadHunt getByUUID(UUID uuid) {

        if (CubeXMCHub.hhunt.get(uuid) == null) {
            HeadHunt bh = new HeadHunt(uuid);
            CubeXMCHub.hhunt.put(uuid, bh);
            return bh;
        }
        if (CubeXMCHub.hhunt.get(uuid) == null) {
            return null;
        }

        return CubeXMCHub.hhunt.get(uuid);

    }

    static void save(Plugin p) {

        for (HeadHunt bh : CubeXMCHub.hhunt.values()) {
            File f = new File(p.getDataFolder() + File.separator + "playerdata" + File.separator + bh.getUUID().toString() + ".yml");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(f);
            ArrayList<String> collectedSkulls = bh.collectedSkulls();
            ArrayList<String> gotAch = bh.getAchievements();

            config.set("inv.items", null);
            config.set("inv.armor", null);
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
    static void load(Plugin p, UUID uuid) {
        File f = new File(p.getDataFolder() + File.separator + "playerdata" + File.separator + uuid.toString() + ".yml");
        if (f.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(f);
            ArrayList<String> collectedSkulls = (ArrayList<String>) config.get("Collected-Skulls");
            ArrayList<String> Achievements = (ArrayList<String>) config.get("Achievements");

            HeadHunt hh = new HeadHunt(uuid);
            if (collectedSkulls != null) {
                for (String cs : collectedSkulls) {
                    if (cs != null)
                        hh.collectSkull(cs);
                }
            }

            if (Achievements != null) {
                for (String a : Achievements) {
                    hh.addAche(a);
                }
            }
            CubeXMCHub.hhunt.put(uuid, hh);
        }
    }

}
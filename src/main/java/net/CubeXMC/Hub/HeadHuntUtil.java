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
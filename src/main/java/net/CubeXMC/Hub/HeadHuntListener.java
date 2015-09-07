package net.CubeXMC.Hub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class HeadHuntListener implements Listener {

    private Inventory test;
    private Inventory test2;
    private Inventory test3;
    private Inventory hhAch;

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.SKULL) {
                Skull b = (Skull) e.getClickedBlock().getState();
                String s = b.getOwner();

                HeadHunt hh = HeadHuntUtil.getByName(p.getName());

                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title " + p.getName() + " title �6Collected Skull!");
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "subtitle " + p.getName() + " title �6" + s);

                if (hh != null && hh.collectSkull(s)) {
                    p.sendMessage(Main.title + ChatColor.GOLD + " You have collected " + ChatColor.GREEN + s + ChatColor.GOLD + "'s Head!");
                    switch (hh.collectedSkulls().size()) {
                        case 5:
                            achMsg(p, "Bounty Hunter!", hh);
                            break;
                        case 10:
                            achMsg(p, "Head Snatcher!", hh);
                            break;
                        case 20:
                            achMsg(p, "The Collector!", hh);
                            break;
                        case 30:
                            achMsg(p, "Heads Much?", hh);
                            break;
                        case 40:
                            achMsg(p, "Heads! Heads! Heads!", hh);
                            break;
                        case 50:
                            achMsg(p, "Getting Head!", hh);
                            break;
                        case 60:
                            achMsg(p, "Still Going?", hh);
                            break;
                        default:
                            break;
                    }
                    hBio(s, p);
                    if (!hh.getAchievements().contains("OwntheOwners") && hh.collectedSkulls().contains("PantherMan594") && hh.collectedSkulls().contains("Vek") && hh.collectedSkulls().contains("SandwichOverdose")) {
                        achMsg(p, "Own the Owners!", hh);
                    }
                    if (!hh.getAchievements().contains("HeadsofAdmins") && hh.collectedSkulls().contains("Major_Dork") && hh.collectedSkulls().contains("Blitzkim2") && hh.collectedSkulls().contains("HYPExMon5ter")) {
                        achMsg(p, "Heads of Admins!", hh);
                    }

                } else {
                    p.sendMessage(Main.title + ChatColor.RED + " You have already collected that player!");
                }
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.getItemInHand().getType() == Material.BOOK) {

                p.openInventory(open());

            }
        }

    }

    private void hBio(String name, Player p) {
        switch (name) {
            case "Jo_Dan":
                Main.logger.info("JO!");
                p.sendMessage(Main.title + ChatColor.WHITE + " Jo_Dan is an amazing builder. And that's an understatement. " +
                        "He probably built everything you see around here, well everything good. If you see him, thank him for his amazing work!");
                break;
            case "Major_Dork":
                p.sendMessage(Main.title + ChatColor.WHITE + " Major_Dork is one of our 3 head admins.");
                break;
            case "HYPExMon5ter":
                p.sendMessage(Main.title + ChatColor.WHITE + " HYPExMon5ter is one of our 3 head admins.");
                break;
            case "Blitzkim2":
                p.sendMessage(Main.title + ChatColor.WHITE + " Blitzkim2 is the first of our 3 head admins. Scratch that. She was the second. " +
                        "The second player (not including the founders) to ever join this server.");
                break;
            case "PantherMan594":
                p.sendMessage(Main.title + ChatColor.WHITE + " Panther is one of the awesome owners of this server, CubeXMC. " +
                        "He works mainly on behind the scenes stuff. You'd better stay away, or his inner panther will come out at you!");
                break;
            case "Vek":
                p.sendMessage(Main.title + ChatColor.WHITE + " Vek is an owner of CubeXMC. And yes, he has HELLA swag. " +
                        "He's the one who manages your donations and puts them into good use for the server. " +
                        "He might also be the reason why you even found this server!");
                break;
            case "SandwichOverdose":
                p.sendMessage(Main.title + ChatColor.WHITE + " An owner of CubeXMC, SandwichOverdose is the guy who will ban you if you make trouble. " +
                        "However he's not all bad, because he'll be the one promoting you if he thinks you are helpful to the server!");
                break;
            case "ShatteredMines2":
                p.sendMessage(Main.title + ChatColor.WHITE + " ShatteredMines2 is one of our many Youtubers, who likes to help out and have fun. " +
                        "His Youtube channel can be found here: https://www.youtube.com/user/DRAGROD1");
                break;
            case "terturl":
                p.sendMessage(Main.title + ChatColor.WHITE + " He is the one that actually started the making of this plugin! He is the one"
                        + " who will most likely see if you good enough for being a dev. He, along with Panther, code all the plugins"
                        + " that our custom on this server.");
            default:
                break;
        }
    }

    private void achMsg(Player p, String name, HeadHunt hh) {
        p.sendMessage(Main.title + ChatColor.GOLD + " Achievement Get!");
        p.sendMessage(Main.title + ChatColor.WHITE + " " + name);
        hh.addAche(name.replace("!", "").replace(" ", "").replace("?", ""));
    }

    private Inventory open() {

        ItemStack border = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 2);
        ItemMeta Border = border.getItemMeta();
        Border.setDisplayName(Main.title);
        border.setItemMeta(Border);

        ItemStack border2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
        ItemMeta Border2 = border2.getItemMeta();
        Border2.setDisplayName(Main.title);
        border2.setItemMeta(Border2);

        ItemStack Skulls = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta skulls = Skulls.getItemMeta();
        skulls.setDisplayName(ChatColor.AQUA + "Check your collected Heads!");
        Skulls.setItemMeta(skulls);

        ItemStack Ach = new ItemStack(Material.PAPER);
        ItemMeta ach = Ach.getItemMeta();
        ach.setDisplayName(ChatColor.AQUA + "Check all those achievements!");
        Ach.setItemMeta(ach);

        test2 = Bukkit.createInventory(null, 27, "CubeX Hub");

        test2.setItem(0, border2);
        test2.setItem(1, border);
        test2.setItem(2, border);
        test2.setItem(3, border);
        test2.setItem(4, border);
        test2.setItem(5, border);
        test2.setItem(6, border);
        test2.setItem(7, border);
        test2.setItem(8, border2);
        test2.setItem(9, border);
        test2.setItem(10, Skulls);
        test2.setItem(16, Ach);
        test2.setItem(17, border);
        test2.setItem(18, border2);
        test2.setItem(19, border);
        test2.setItem(20, border);
        test2.setItem(21, border);
        test2.setItem(22, border);
        test2.setItem(23, border);
        test2.setItem(24, border);
        test2.setItem(25, border);
        test2.setItem(26, border2);

        // 0  1  2  3  4  5  6  7  8
        // 9 10 11 12 13 14 15 16 17
        //18 19 20 21 22 23 24 25 26

        return test2;
    }

    private Inventory openAch(Player p) {

        ItemStack hh = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta Hh = hh.getItemMeta();
        HeadHunt hH = HeadHuntUtil.getByName(p.getName());
        Hh.setDisplayName(ChatColor.GOLD + "Head Hunt");
        hh.setItemMeta(Hh);

        test3 = Bukkit.createInventory(null, 27, "Achievements");

        ItemStack border = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 2);
        ItemMeta Border = border.getItemMeta();
        Border.setDisplayName(Main.title);
        border.setItemMeta(Border);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Collect all 3 Owners' heads!");

        ItemStack h1 = new ItemStack(Material.COAL);
        ItemMeta H1 = h1.getItemMeta();
        H1.setDisplayName(ChatColor.DARK_RED + "Own the Owners!");
        H1.setLore(lore);
        h1.setItemMeta(H1);

        ItemStack h11 = new ItemStack(Material.DIAMOND);
        ItemMeta H11 = h11.getItemMeta();
        H11.setDisplayName(ChatColor.GREEN + "Own the Owners!");
        H11.setLore(lore);
        h11.setItemMeta(H11);

        lore.set(0, ChatColor.BLUE + "Collect all 3 Head Admins' heads!");

        ItemStack h2 = new ItemStack(Material.COAL);
        ItemMeta H2 = h2.getItemMeta();
        H2.setDisplayName(ChatColor.DARK_RED + "Heads of Admins!");
        H2.setLore(lore);
        h2.setItemMeta(H2);

        ItemStack h22 = new ItemStack(Material.DIAMOND);
        ItemMeta H22 = h22.getItemMeta();
        H22.setDisplayName(ChatColor.GREEN + "Heads of Admins!");
        H22.setLore(lore);
        h22.setItemMeta(H22);

        if (hH != null && !hH.getAchievements().contains("OwntheOwners"))
            test3.setItem(0, h1);
        else
            test3.setItem(0, h11);
        if (hH != null && !hH.getAchievements().contains("HeadsofAdmins"))
            test3.setItem(1, h2);
        else
            test3.setItem(1, h22);
        test3.setItem(2, border);
        test3.setItem(3, border);
        test3.setItem(4, border);
        test3.setItem(5, border);
        test3.setItem(6, border);
        test3.setItem(7, border);
        test3.setItem(8, border);
        test3.setItem(9, border);
        test3.setItem(10, border);
        test3.setItem(11, border);
        test3.setItem(12, border);
        test3.setItem(13, hh);
        test3.setItem(14, border);
        test3.setItem(15, border);
        test3.setItem(16, border);
        test3.setItem(17, border);
        test3.setItem(18, border);
        test3.setItem(19, border);
        test3.setItem(20, border);
        test3.setItem(21, border);
        test3.setItem(22, border);
        test3.setItem(23, border);
        test3.setItem(24, border);
        test3.setItem(25, border);
        test3.setItem(26, border);

        return test3;
    }

    private Inventory hhAch(Player p) {

        ItemStack border = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 2);
        ItemMeta Border = border.getItemMeta();
        Border.setDisplayName(Main.title);
        border.setItemMeta(Border);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Collect 5 heads!");

        ItemStack h1 = new ItemStack(Material.COAL);
        ItemMeta H1 = h1.getItemMeta();
        H1.setDisplayName(ChatColor.DARK_RED + "Bounty Hunter");
        H1.setLore(lore);
        h1.setItemMeta(H1);

        ItemStack h11 = new ItemStack(Material.DIAMOND);
        ItemMeta H11 = h11.getItemMeta();
        H11.setDisplayName(ChatColor.GREEN + "Bounty Hunter");
        H11.setLore(lore);
        h11.setItemMeta(H11);

        lore.set(0, ChatColor.BLUE + "Collect 10 heads!");

        ItemStack h2 = new ItemStack(Material.COAL);
        ItemMeta H2 = h2.getItemMeta();
        H2.setDisplayName(ChatColor.DARK_RED + "Head Snatcher");
        H2.setLore(lore);
        h2.setItemMeta(H2);

        ItemStack h22 = new ItemStack(Material.DIAMOND);
        ItemMeta H22 = h22.getItemMeta();
        H22.setDisplayName(ChatColor.GREEN + "Head Snatcher");
        H22.setLore(lore);
        h22.setItemMeta(H22);

        lore.set(0, ChatColor.BLUE + "Collect 20 heads!");

        ItemStack h3 = new ItemStack(Material.COAL);
        ItemMeta H3 = h3.getItemMeta();
        H3.setDisplayName(ChatColor.DARK_RED + "The Collector");
        H3.setLore(lore);
        h3.setItemMeta(H3);

        ItemStack h33 = new ItemStack(Material.DIAMOND);
        ItemMeta H33 = h33.getItemMeta();
        H33.setDisplayName(ChatColor.GREEN + "The Collector");
        H33.setLore(lore);
        h33.setItemMeta(H33);

        lore.set(0, ChatColor.BLUE + "Collect 30 heads!");

        ItemStack h4 = new ItemStack(Material.COAL);
        ItemMeta H4 = h4.getItemMeta();
        H4.setDisplayName(ChatColor.DARK_RED + "Heads Much?");
        H4.setLore(lore);
        h4.setItemMeta(H4);

        ItemStack h44 = new ItemStack(Material.DIAMOND);
        ItemMeta H44 = h44.getItemMeta();
        H44.setDisplayName(ChatColor.GREEN + "Heads Much?");
        H44.setLore(lore);
        h44.setItemMeta(H44);

        lore.set(0, ChatColor.BLUE + "Collect 40 heads!");

        ItemStack h5 = new ItemStack(Material.COAL);
        ItemMeta H5 = h5.getItemMeta();
        H5.setDisplayName(ChatColor.DARK_RED + "Heads! Heads! Heads!");
        H5.setLore(lore);
        h5.setItemMeta(H5);

        ItemStack h55 = new ItemStack(Material.DIAMOND);
        ItemMeta H55 = h55.getItemMeta();
        H55.setDisplayName(ChatColor.GREEN + "Heads! Heads! Heads");
        H55.setLore(lore);
        h55.setItemMeta(H55);

        lore.set(0, ChatColor.BLUE + "Collect 50 heads!");

        ItemStack h6 = new ItemStack(Material.COAL);
        ItemMeta H6 = h6.getItemMeta();
        H6.setDisplayName(ChatColor.DARK_RED + "Getting Head!");
        H6.setLore(lore);
        h6.setItemMeta(H6);

        ItemStack h66 = new ItemStack(Material.DIAMOND);
        ItemMeta H66 = h66.getItemMeta();
        H66.setDisplayName(ChatColor.GREEN + "Getting Head!");
        H66.setLore(lore);
        h66.setItemMeta(H66);

        lore.set(0, ChatColor.BLUE + "Collect 60 heads!");

        ItemStack h7 = new ItemStack(Material.COAL);
        ItemMeta H7 = h7.getItemMeta();
        H7.setDisplayName(ChatColor.DARK_RED + "Still Going?");
        H7.setLore(lore);
        h7.setItemMeta(H7);

        ItemStack h77 = new ItemStack(Material.DIAMOND);
        ItemMeta H77 = h77.getItemMeta();
        H77.setDisplayName(ChatColor.GREEN + "Still Going?");
        H77.setLore(lore);
        h77.setItemMeta(H77);

        hhAch = Bukkit.createInventory(null, 27, "HeadHunt Achievements");

        HeadHunt hh = HeadHuntUtil.getByName(p.getName());

        if (hh != null && !hh.getAchievements().contains("BountyHunter"))
            hhAch.setItem(2, h1);
        else
            hhAch.setItem(2, h11);
        if (hh != null && !hh.getAchievements().contains("HeadSnatcher"))
            hhAch.setItem(3, h2);
        else
            hhAch.setItem(3, h22);
        hhAch.setItem(0, border);
        hhAch.setItem(1, border);
        hhAch.setItem(5, border);
        hhAch.setItem(6, border);
        hhAch.setItem(7, border);
        hhAch.setItem(8, border);
        hhAch.setItem(9, border);
        hhAch.setItem(10, border);
        hhAch.setItem(11, border);
        hhAch.setItem(12, border);
        hhAch.setItem(14, border);
        hhAch.setItem(15, border);
        hhAch.setItem(16, border);
        if (hh != null && !hh.getAchievements().contains("TheCollector"))
            hhAch.setItem(4, h3);
        else
            hhAch.setItem(4, h33);
        if (hh != null && !hh.getAchievements().contains("HeadsMuch"))
            hhAch.setItem(13, h4);
        else
            hhAch.setItem(13, h44);
        if (hh != null && !hh.getAchievements().contains("HeadsHeadsHeads"))
            hhAch.setItem(22, h5);
        else
            hhAch.setItem(22, h55);
        if (hh != null && !hh.getAchievements().contains("GettingHead"))
            hhAch.setItem(23, h6);
        else
            hhAch.setItem(23, h66);
        if (hh != null && !hh.getAchievements().contains("StillGoing"))
            hhAch.setItem(24, h7);
        else
            hhAch.setItem(24, h77);
        hhAch.setItem(17, border);
        hhAch.setItem(18, border);
        hhAch.setItem(19, border);
        hhAch.setItem(20, border);
        hhAch.setItem(21, border);
        hhAch.setItem(25, border);
        hhAch.setItem(26, border);


        // 0  1  2  3  4  5  6  7  8
        // 9 10 11 12 13 14 15 16 17
        //18 19 20 21 22 23 24 25 26

        return hhAch;
    }

    private Inventory openSkulls(Player p) {

        HeadHunt hh = HeadHuntUtil.getByName(p.getName());
        if (hh != null) {
            ArrayList<String> s = hh.collectedSkulls();

            int j = 0;

            test = Bukkit.createInventory(null, 54, "Collected Skulls: " + s.size());

            while (s.size() > j) {

                ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setOwner(s.get(j));
                meta.setDisplayName(ChatColor.GOLD + s.get(j));
                item.setItemMeta(meta);

                test.setItem(j, item);

                j++;
            }

            return test;
        }
        return null;
    }

    @EventHandler
    public void interact(InventoryClickEvent e) {

        if (test2 != null && Objects.equals(e.getInventory().getName(), test2.getName())) {

            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains("achievements!")) {
                e.getWhoClicked().openInventory(openAch((Player) e.getWhoClicked()));
            }

            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Heads!")) {
                e.getWhoClicked().openInventory(openSkulls((Player) e.getWhoClicked()));
            }
            e.setCancelled(true);
        }

        if (test != null && Objects.equals(e.getInventory().getName(), test.getName())) {
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {
                String hNameStrip = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
                Main.logger.info(hNameStrip);
                if (Main.bioHeads.contains(hNameStrip)) {
                    hBio(hNameStrip, (Player) e.getWhoClicked());
                }
            }
            e.setCancelled(true);
        }

        if (test3 != null && Objects.equals(e.getInventory().getName(), test3.getName())) {
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Head Hunt")) {
                e.getWhoClicked().openInventory(hhAch((Player) e.getWhoClicked()));
            }
            e.setCancelled(true);
        }

        if (hhAch != null && Objects.equals(e.getInventory().getName(), hhAch.getName())) {
            e.setCancelled(true);
        }
    }

}
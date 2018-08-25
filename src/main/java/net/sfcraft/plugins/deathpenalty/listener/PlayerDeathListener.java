package net.sfcraft.plugins.deathpenalty.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;

import com.meowj.langutils.lang.LanguageHelper;
import com.meowj.langutils.locale.LocaleHelper;

import net.milkbowl.vault.economy.Economy;
import net.sfcraft.plugins.deathpenalty.DeathPenalty;

public class PlayerDeathListener implements Listener {
    private final DeathPenalty plugin;
    private final Economy economy;

    public PlayerDeathListener(DeathPenalty instance) {
        plugin = instance;
        economy = plugin.getEconomy();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        boolean haveLanguageUtils = Bukkit.getPluginManager().getPlugin("LangUtils") != null;
        String locale = haveLanguageUtils ? LocaleHelper.getPlayerLanguage(player) : "zh_CN";
        List<String> noPluginWorlds = plugin.getSetting().getWorlds();
        String playerWorld = player.getWorld().getName();
        for (String string : noPluginWorlds) {
            if (string.equalsIgnoreCase(playerWorld)) {
                return;
            }
        }
        // event.setKeepInventory(true); // 设置死亡不掉落
        List<ItemStack> dropItem = dropItem(player, event);
        double dropMoney = dropMoney(player);
        int droppedExp = dropExp(player, event);

        String message = this.plugin.getSetting().getMessage_head() + this.plugin.getSetting().getMessage_deathDrop();
        if (((dropItem != null) && (!dropItem.isEmpty())) || (dropMoney != 0.0D) || (droppedExp != 0)) {
            if ((int) dropMoney != 0) {
                message = message + "§r" + this.plugin.getSetting().getMessage_money() + "§b" + (int) dropMoney
                        + "§r， ";
            }
            if (droppedExp != 0) {
                message = message + "§b" + droppedExp + " §r" + this.plugin.getSetting().getMessage_exp() + "§r， ";
            }
            if (this.plugin.getSetting().getBroadcast().booleanValue()) {
                Bukkit.broadcastMessage(
                        message.substring(0, message.length() - 2).replaceAll("%player%", player.getDisplayName()));
            } else {
                player.sendMessage(message.substring(0, message.length() - 2).replaceAll("%player%", "你"));
            }
            if ((dropItem != null) && (!dropItem.isEmpty())) {
                message = this.plugin.getSetting().getMessage_head();
                for (ItemStack itemStack : dropItem) {
                    String itemName = haveLanguageUtils ? LanguageHelper.getItemDisplayName(itemStack, locale)
                            : itemStack.getType().name();
                    message = message + "§b" + itemStack.getAmount() + " §r" + itemName + "§r， ";
                }
                if (this.plugin.getSetting().getBroadcast().booleanValue()) {
                    Bukkit.broadcastMessage(
                            message.substring(0, message.length() - 2).replaceAll("%player%", player.getDisplayName()));
                } else {
                    player.sendMessage(message.substring(0, message.length() - 2).replaceAll("%player%", "你"));
                }
            }
        }

    }

    /**
     * 掉落物品
     * 
     * @param player
     * @param event
     */
    private List<ItemStack> dropItem(final Player player, final PlayerDeathEvent event) {
        if (player.hasPermission("DeathPenalty.item") || player.isOp()) {
            return null;
        }
        int dropItemAcount = event.getDrops().size();
        int dropItemNum = plugin.getSetting().getItem(); // 掉落物品格数
        if (dropItemNum == 0) {
            return null;
        }
        List<String> bypassItems = plugin.getSetting().getBypass();
        List<ItemStack> dropItems = new ArrayList<ItemStack>();
        int times = 0;
        if (dropItemNum >= dropItemAcount) {
            dropItems = event.getDrops();
        } else {
            while (dropItems.size() < dropItemNum) {
                int randomNumber = (int) Math.round(Math.random() * (dropItemAcount - 1));
                ItemStack dropItem = event.getDrops().get(randomNumber);
                if (!dropItems.contains(dropItem)) {
                    dropItems.add(dropItem);
                }
                if (++times > dropItemNum * 2) {
                    break;
                } // 防止背包里有大量相同物品，导致取不够掉落物，死循环的BUG
            }
        }

        Bukkit.getLogger().info("[DeathPenalty] Drop list got after " + times + " times");

        Location location = player.getLocation();
        for (int i = 0; i < dropItems.size(); i++) {
            ItemStack temp = dropItems.get(i);
            boolean bypass = false;
            for (String item : bypassItems)
                if (Integer.parseInt(item) == temp.getTypeId()) {
                    bypass = true;
                }
            if (bypass) {
                Bukkit.getLogger().info("[DeathPenalty] Found " + temp.getTypeId() + " at " + i + "/" + dropItems.size()
                        + ", delete it");
                dropItems.remove(temp);
                i--;
            }
        }
        HashMap<Integer, ItemStack> a = player.getInventory()
                .removeItem(dropItems.toArray(new ItemStack[dropItems.size()]));
        dropItems.removeAll(a.values()); // 装备栏的物品没法删除，不生成掉落物防止刷物品
        for (ItemStack temp : dropItems)
            location.getWorld().dropItemNaturally(location, temp);// 生成掉落物
        return dropItems;
    }

    /**
     * 掉落金钱,需前置插件vault,及任意一款经济插件
     * 
     * @param player
     * @return
     */
    private double dropMoney(final Player player) {
        if (player.hasPermission("DeathPenalty.money") || player.isOp()) {
            return 0;
        }
        if (this.economy == null) {
            return 0;
        }

        Location location = player.getLocation();
        double playerMoney = this.economy.getBalance(player);
        String dropMoneySetting = plugin.getSetting().getMoney();
        double dropMoney = 0;
        if (dropMoneySetting.indexOf("%") != -1) {
            dropMoney = playerMoney * Double.parseDouble(dropMoneySetting.replace("%", "")) / 100;
        } else {
            dropMoney = Double.parseDouble(dropMoneySetting);
        }
        if (this.economy.has(player, dropMoney)) {
            this.economy.withdrawPlayer(player, dropMoney);
        } else {
            dropMoney = 0;
        }

        double dropNote = dropMoney;
        while (dropNote >= 1000) {
            ItemStack temp = new ItemStack(Material.PAPER, 1);
            temp.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
            ItemMeta itemMeta = temp.getItemMeta();
            itemMeta.setUnbreakable(true);
            itemMeta.setDisplayName("§0§1§2§3§4§5§7$1000 Note");
            itemMeta.setLore(Arrays.asList("§oPromises to pay the bearer on demand at its office here"));
            temp.setItemMeta(itemMeta);
            location.getWorld().dropItemNaturally(location, temp);// 生成掉落物
            dropNote += -1000;
        }
        while (dropNote >= 100) {
            ItemStack temp = new ItemStack(Material.PAPER, 1);
            temp.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
            ItemMeta itemMeta = temp.getItemMeta();
            itemMeta.setUnbreakable(true);
            itemMeta.setDisplayName("§0§1§2§3§4§5§c$100 Note");
            itemMeta.setLore(Arrays.asList("§oPromises to pay the bearer on demand at its office here"));
            temp.setItemMeta(itemMeta);
            location.getWorld().dropItemNaturally(location, temp);// 生成掉落物
            dropNote += -100;
        }
        while (dropNote >= 50) {
            ItemStack temp = new ItemStack(Material.PAPER, 1);
            temp.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
            ItemMeta itemMeta = temp.getItemMeta();
            itemMeta.setUnbreakable(true);
            itemMeta.setDisplayName("§0§1§2§3§4§5§e$50 Note");
            itemMeta.setLore(Arrays.asList("§oPromises to pay the bearer on demand at its office here"));
            temp.setItemMeta(itemMeta);
            location.getWorld().dropItemNaturally(location, temp);// 生成掉落物
            dropNote += -50;
        }
        while (dropNote >= 20) {
            ItemStack temp = new ItemStack(Material.PAPER, 1);
            temp.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
            ItemMeta itemMeta = temp.getItemMeta();
            itemMeta.setUnbreakable(true);
            itemMeta.setDisplayName("§0§1§2§3§4§5§6$20 Note");
            itemMeta.setLore(Arrays.asList("§oPromises to pay the bearer on demand at its office here"));
            temp.setItemMeta(itemMeta);
            location.getWorld().dropItemNaturally(location, temp);// 生成掉落物
            dropNote += -20;
        }
        while (dropNote >= 10) {
            ItemStack temp = new ItemStack(Material.PAPER, 1);
            temp.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
            ItemMeta itemMeta = temp.getItemMeta();
            itemMeta.setUnbreakable(true);
            itemMeta.setDisplayName("§0§1§2§3§4§5§b$10 Note");
            itemMeta.setLore(Arrays.asList("§oPromises to pay the bearer on demand at its office here"));
            temp.setItemMeta(itemMeta);
            location.getWorld().dropItemNaturally(location, temp);// 生成掉落物
            dropNote += -10;
        }

        return dropMoney;
    }

    /**
     * 掉落经验
     * 
     * @param player
     * @param event
     * @return
     */
    private int dropExp(final Player player, final PlayerDeathEvent event) {
        if (player.hasPermission("DeathPenalty.exp") || player.isOp()) {
            event.setKeepLevel(true);
            event.setDroppedExp(0);
            return 0;
        }
        int totalExp = player.getTotalExperience();
        int droppedExp = 0;
        String dropExpSetting = plugin.getSetting().getExp();
        if (dropExpSetting.indexOf("%") != -1) {
            droppedExp = (int) (totalExp * Double.parseDouble(dropExpSetting.replace("%", ""))) / 100;
        } else {
            droppedExp = Integer.parseInt(dropExpSetting);
        }
        if (plugin.getSetting().getSummonxp() == true)
            event.setDroppedExp(totalExp - droppedExp);
        else
            event.setDroppedExp(0);
        // event.setNewExp(totalExp - droppedExp); 原命令可能导致刷经验，改用Ess命令
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "exp set " + player.getName() + " " + String.valueOf(totalExp - droppedExp));
        return droppedExp;
    }
}

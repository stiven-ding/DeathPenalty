package net.sfcraft.plugins.deathpenalty.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import net.sfcraft.plugins.deathpenalty.DeathPenalty;

public class CommandManager implements CommandExecutor, TabCompleter {
	private final DeathPenalty plugin;

	public CommandManager(DeathPenalty instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase("DeathPenalty")) {
			return false;
		}
		if (args.length < 1) {
			sender.sendMessage("[DeathPenalty] Use '/dp reload' to reolad config");
			return true;
		}
		if (args[0].equalsIgnoreCase("reload")) {
			if (!sender.hasPermission("DeathPenalty.reload") || !sender.isOp()) {
				sender.sendMessage("[DeathPenalty] I'm sorry, but you do not have permossion to perform this command.");
				return true;
			}
			plugin.reloadConfig();
			plugin.loadConfig();
			sender.sendMessage("[DeathPenalty] Configuration has been reloaded.");
			return true;
		}
		if (args[0].equalsIgnoreCase("summon")) {
			if (!sender.hasPermission("DeathPenalty.reload") || !sender.isOp()) {
				sender.sendMessage("[DeathPenalty] I'm sorry, but you do not have permossion to perform this command.");
				return true;
			}
			Player player = (Player)sender;
			Location location = player.getLocation();
			double dropNote = Integer.parseInt(args[1]);
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
				sender.sendMessage("drop 1000");
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
				sender.sendMessage("drop 100");
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
				sender.sendMessage("drop 50");
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
				sender.sendMessage("drop 20");
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
				sender.sendMessage("drop 10");
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase("DeathPenalty")) {
			return null;
		}
		for (int i = 0; i < args.length; i++) {
			args[i] = args[i].toLowerCase();
		}
		if (args.length == 1) {
			List<String> completions = new ArrayList<String>();
			completions.add("reload");
			return completions;
		}
		return null;
	}
}

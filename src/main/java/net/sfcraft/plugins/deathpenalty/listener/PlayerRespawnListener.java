package net.sfcraft.plugins.deathpenalty.listener;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.sfcraft.plugins.deathpenalty.DeathPenalty;
import net.sfcraft.plugins.deathpenalty.DeathPenaltyRunnable;

public class PlayerRespawnListener implements Listener {
	private final DeathPenalty plugin;

	public PlayerRespawnListener(DeathPenalty instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		List<String> noPluginWorlds = plugin.getSetting().getWorlds();
		String playerWorld = player.getWorld().getName();
		for (String string : noPluginWorlds) {
			if (string.equalsIgnoreCase(playerWorld)) {
				return;
			}
		}
		giveBuff(player);
	}

	/**
	 * 给BUFF
	 * 
	 * @param player
	 */
	private void giveBuff(final Player player) {
		if (player.hasPermission("DeathPenalty.buff") || player.isOp()) {
			return;
		}
		new DeathPenaltyRunnable(plugin) {
			public void run() {
				int SLOW_TIME = plugin.getSetting().getBuff_slow();
				int BLINDNESS_TIME = plugin.getSetting().getBuff_blindness();
				int CONFUSION_TIME = plugin.getSetting().getBuff_confusion();
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, SLOW_TIME * 20, 1));
				player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, BLINDNESS_TIME * 20, 1));
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, CONFUSION_TIME * 20, 1));
				player.sendMessage(plugin.getSetting().getMessage_head() + plugin.getSetting().getMessage_givebuff());
			}
		}.runTaskLater(plugin, 10L);
	}
}

package net.sfcraft.plugins.deathpenalty;

import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.sfcraft.plugins.deathpenalty.config.DeathPenaltySetting;
import net.sfcraft.plugins.deathpenalty.listener.CommandManager;
import net.sfcraft.plugins.deathpenalty.listener.PlayerDeathListener;
import net.sfcraft.plugins.deathpenalty.listener.PlayerRespawnListener;

public class DeathPenalty extends JavaPlugin {
	private DeathPenaltySetting setting;
	private Economy economy;
	private PlayerDeathListener deathListener;
	private PlayerRespawnListener respawnListener;

	/**
	 * 插件载入
	 */
	public void onEnable() {
		loadConfig();
		this.setEconomy();
        PluginManager pm = getServer().getPluginManager();
        deathListener = new PlayerDeathListener(this);
        respawnListener = new PlayerRespawnListener(this);
        pm.registerEvents(deathListener, this);
        pm.registerEvents(respawnListener, this);
        getCommand("DeathPenalty").setExecutor(new CommandManager(this));
		getLogger().info(getDescription().getName() + " V" +  getDescription().getVersion() + " Has Been Enabled");
	}

	/**
	 * 插件卸载
	 */
	public void onDisable() {
		PlayerDeathEvent.getHandlerList().unregister(deathListener);
		PlayerRespawnEvent.getHandlerList().unregister(respawnListener);
		getLogger().info(getDescription().getName() + " V" +  getDescription().getVersion() + " Has Been Disabled");
	}

	/**
	 * 加载配置文件
	 */
	public void loadConfig() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
			saveDefaultConfig();
		}
		boolean needNewConfig = false;
		setting = new DeathPenaltySetting();
		FileConfiguration config = getConfig();
		String regex = "^[0-9]+[.]{0,1}[0-9]{1,2}[%]{0,1}$";
		String money = config.getString("Money");
		if(!Pattern.matches(regex, money)){
			needNewConfig = true;
			config.set("Money", setting.getMoney());
		} else {
			setting.setMoney(money);
		}
		String exp = config.getString("Exp");
		if(!Pattern.matches(regex, exp)){
			needNewConfig = true;
			config.set("Exp", setting.getExp());
		} else {
			setting.setExp(exp);
		}
		int item = config.getInt("Item");
		setting.setItem(item);
		int buff_slow = config.getInt("Buff.SlowTime");
		setting.setBuff_slow(buff_slow);
		int buff_blindness = config.getInt("Buff.BlindnessTime");
		setting.setBuff_blindness(buff_blindness);
		int buff_confusion = config.getInt("Buff.ConfusionTime");
		setting.setBuff_confusion(buff_confusion);
		List<String> worlds = config.getStringList("Worlds");
		setting.setWorlds(worlds);
		String message_deathDrop = config.getString("Message.DeathDrop");
		setting.setMessage_deathDrop(message_deathDrop);
		String message_money = config.getString("Message.Money");
		setting.setMessage_money(message_money);
		String message_exp = config.getString("Message.Exp");
		setting.setMessage_exp(message_exp);
		String message_givebuff = config.getString("Message.GiveBuff");
		setting.setMessage_givebuff(message_givebuff);
		String message_head = config.getString("Message.Head");
		setting.setMessage_head(message_head);
		Boolean summonxp = config.getBoolean("Summonxp");
		setting.setSummonxp(summonxp);
		List<String> bypass_item = config.getStringList("Bypass");
		setting.setBypass(bypass_item);
		if(needNewConfig){
			saveConfig();
		}
	}
	
	/**
	 * 设置经济插件
	 */
	private void setEconomy() {
		if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
			if (economyProvider != null) {
				this.economy = ((Economy) economyProvider.getProvider());
			}
		}
	}

	public DeathPenaltySetting getSetting() {
		return setting;
	}
	
	public Economy getEconomy() {
		return economy;
	}
}

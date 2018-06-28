package net.sfcraft.plugins.deathpenalty.config;

import java.util.ArrayList;
import java.util.List;

public class DeathPenaltySetting {
	private String money = "10%";		//掉落金额
	private String exp = "10%";		//掉落经验
	private int item = 4;			//掉落物品格数
	private int buff_slow = 120;		//缓慢状态持续时间
	private int buff_blindness = 120;	//失明状态持续时间
	private int buff_confusion = 120;	//反胃状态持续时间+
	private List<String> bypass_item = new ArrayList<String>();
	private List<String> worlds = new ArrayList<String>();	//插件不生效的世界名集合
	private String message_head = "§d 死亡惩罚 §c§l>> ";
	private String message_deathDrop = "§d 死亡惩罚 §c§l>> §d你因为死亡掉落了:";
	private String message_money = "$";
	private String message_exp = "经验";
	private String message_givebuff = "§d 死亡惩罚 §c§l>> §d重生后你感到非常虚弱.";
	private Boolean summonxp = false;
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public int getBuff_slow() {
		return buff_slow;
	}
	public void setBuff_slow(int buff_slow) {
		this.buff_slow = buff_slow;
	}
	public int getBuff_blindness() {
		return buff_blindness;
	}
	public void setBuff_blindness(int buff_blindness) {
		this.buff_blindness = buff_blindness;
	}
	public int getBuff_confusion() {
		return buff_confusion;
	}
	public void setBuff_confusion(int buff_confusion) {
		this.buff_confusion = buff_confusion;
	}
	public List<String> getWorlds() {
		return worlds;
	}
	public void setWorlds(List<String> worlds) {
		this.worlds = worlds;
	}
	public String getMessage_deathDrop() {
		return message_deathDrop;
	}
	public void setMessage_deathDrop(String message_deathDrop) {
		this.message_deathDrop = message_deathDrop;
	}
	public String getMessage_money() {
		return message_money;
	}
	public void setMessage_money(String message_money) {
		this.message_money = message_money;
	}
	public String getMessage_head() {
		return message_head;
	}
	public void setMessage_head(String message_head) {
		this.message_head = message_head;
	}
	public String getMessage_exp() {
		return message_exp;
	}
	public void setMessage_exp(String message_exp) {
		this.message_exp = message_exp;
	}
	public String getMessage_givebuff() {
		return message_givebuff;
	}
	public void setMessage_givebuff(String message_givebuff) {
		this.message_givebuff = message_givebuff;
	}
	public void setSummonxp(Boolean drop) {
		summonxp = drop;
	}
	public Boolean getSummonxp() {
		return summonxp;
	}
	public void setBypass(List<String> bypass) {
		bypass_item = bypass;
	}
	public List<String> getBypass() {
		return bypass_item;
	}
}

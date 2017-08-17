package me.smiileyface.backend.players;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.smiileyface.utils.ChatUtils;

public enum Rank {

	OWNER(9, "Owner", ChatColor.DARK_RED),
	DEV(8, "Developer", ChatColor.DARK_RED),
	ADMIN(7, "Admin", ChatColor.DARK_RED),
	SR_MOD(6, "Sr.Moderator", ChatColor.DARK_RED),
	MOD(5, "Moderator", ChatColor.RED),
	VIP(4, "VIP", ChatColor.DARK_PURPLE),
	DIAMOND(3, "Diamond", ChatColor.DARK_AQUA),
	GOLD(2, "Gold", ChatColor.GOLD),
	IRON(1, "Iron", ChatColor.GRAY),
	REGULAR(0, "Regular", ChatColor.BLUE);
	
	private int id;
	private String name;
	private ChatColor color;
	
	Rank(int id, String name, ChatColor color) {
		this.id = id;
		this.name = name;
		this.color = color;
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
	public String getPrefix() {
		if(name.equals(Rank.OWNER.getName()) || name.equals(Rank.DEV.getName()) || name.equals(Rank.ADMIN.getName()))
			return color + "" + ChatColor.BOLD;
		return color + "";
	}
	
	public String getSuffix() {
		if(name.equals(Rank.MOD.getName()) ||name.equals(Rank.SR_MOD.getName()) || name.equals(Rank.ADMIN.getName()) || name.equals(Rank.DEV.getName()) || name.equals(Rank.OWNER.getName())) {
			return ChatColor.AQUA + "";
		}
		return "";
	}
	
	public boolean has(Player player, Rank rank, boolean inform) {
		if(compareTo(rank) <= 0)
			return true;
		else if(inform && compareTo(rank) > 0) {
			player.sendMessage(ChatUtils.modulate("SurvivalGames", "&cYou do not have have permission to do this."));
			return false;
		}
		return false;
	}
}

package me.smiileyface.mcsg.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.smiileyface.backend.Players;
import me.smiileyface.mcsg.Core;
import me.smiileyface.mcsg.game.Game;
import me.smiileyface.mcsg.player.GamePlayer;
import me.smiileyface.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;

public class ChatListener implements Listener {

	private static String design = ChatUtils.colorize("&8(&e$t&8)&r$s$p&8> &r$m");
	private static String specPrefix = ChatColor.DARK_RED + "SPEC" + ChatColor.DARK_GRAY + ":";
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if(!e.isCancelled()) {
			GamePlayer player = Players.getData().getPlayerFromUUID(e.getPlayer());
			
			String format = design;
			format = format.replace("$t", player.getPoints() + "");
			format = format.replace("$s", player.isInSpecChat() ? specPrefix : "");
			format = format.replace("$p", player.getRank().getPrefix() + player.getRealName());
			format = format.replace("$m", player.getRank().getSuffix() + e.getMessage());
			
			System.out.println(format);
			
			e.setCancelled(true);
			Game g = Core.get().getGame();
			
			if(player.isInSpecChat()) {
				for(GamePlayer p : g.getSpectators()) {
					p.getPlayer().sendMessage(format);
				}
			} else {
				Bukkit.broadcastMessage(format);
			}
		}
	}
}

package me.smiileyface.mcsg.game.phase;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.smiileyface.mcsg.Core;
import me.smiileyface.mcsg.game.Game;
import me.smiileyface.mcsg.game.GameState;
import me.smiileyface.utils.ChatUtils;
import me.smiileyface.utils.ZipUtils;

public class EndPhase {

	private Game game;
	private int time = 20;
	private BukkitTask task;
	
	public EndPhase(Game game) {
		this.game = game;
	}
	
	public void load() {
		game.setState(GameState.ENDING);
		Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "&3The games are over and will restart soon..."));
		
		task = Bukkit.getScheduler().runTaskTimer(Core.get(), new Runnable() {
			
			@Override
			public void run() {
				if(time == 10) {
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "The survival games are restarting..."));
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "&4The server is restarting. Please join back and check http://mcsg.rip for more servers."));
				}
				if(time == 5) {
					for(Player player : Bukkit.getOnlinePlayers()) {
						player.kickPlayer(ChatUtils.colorize("The server is restarting. \n Please join back and check http://www.mscg.rip for more servers."));
					}
					try {
						ZipUtils.delete(new File(game.getCurrentMap().getName()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				if(time == 0) {
					task.cancel();
					game.setState(GameState.DEAD);
					Bukkit.shutdown();
					return;
				}
			}
			
		}, 20L, 20L);
	}
}

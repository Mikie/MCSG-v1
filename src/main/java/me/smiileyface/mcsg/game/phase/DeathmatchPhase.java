package me.smiileyface.mcsg.game.phase;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.smiileyface.mcsg.Core;
import me.smiileyface.mcsg.game.Game;
import me.smiileyface.mcsg.game.GameState;
import me.smiileyface.mcsg.player.GamePlayer;
import me.smiileyface.utils.ChatUtils;
import me.smiileyface.utils.TimeUtils;

public class DeathmatchPhase {

	private int time = 600, starttime = time;
	private BukkitTask task;
	private Game game;
	private boolean running;
	
	public DeathmatchPhase(Game game) {
		this.game = game;
	}
	
	public void load() {
		start();
	}
	
	public void start() {
		running = true;
		
		Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "&4The time limit has been reached."));
		Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "&4We will now begin the final deathmatch."));
		Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "&3Now now now, President Snow doesn't like when tributes run..."));
		
		game.setState(GameState.DEATHMATCH);
		
		for(GamePlayer player : game.getPlayers()) {
			player.getPlayer().teleport(game.getMapManager().getSpawns(game.getCurrentMap()).get(player.getSpawnIndex()));
		}
		
		for(GamePlayer player : game.getSpectators()) {
			player.getPlayer().teleport(game.getMapManager().getSpawns(game.getCurrentMap()).get(0));
			Vector v = new Vector(0, 2, 0);
			v.multiply(1.25);
			player.getPlayer().getLocation().setDirection(v);
		}
		
		task = Bukkit.getScheduler().runTaskTimer(Core.get(), new Runnable() {
			
			@Override
			public void run() {
				if(time == 60) {
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", TimeUtils.formatTime(time) + " left in the deathmatch!"));
				}
				
				if(time % 60 == 0 && time != 0) {
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", TimeUtils.formatTime(time) + " left in the deathmatch!"));
				} else if(time % 10 == 0 && time < 60 && time > 10) {
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", TimeUtils.formatTime(time) + " left in the deathmatch!"));
				} else if(time <= 10 && time > 0) {
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", TimeUtils.formatTime(time) + " left in the deathmatch!"));
				} else if(time == 0) {
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "&The time limit has been reached."));
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "&There is no winner..."));
					game.end();
				}
				time--;
			}
			
		}, 0L, 20L);
	}
	
	public int getTime() {
		return time;
	}
	
	public void cancelTask() {
		if(task != null)
			task.cancel();
		running = false;
	}
	
	public int getStartTime() {
		return starttime;
	}
	
	public boolean isRunning() {
		return running;
	}
}

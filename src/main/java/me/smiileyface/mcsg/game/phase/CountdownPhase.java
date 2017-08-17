package me.smiileyface.mcsg.game.phase;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import me.smiileyface.mcsg.Core;
import me.smiileyface.mcsg.game.Game;
import me.smiileyface.mcsg.game.GameState;
import me.smiileyface.mcsg.map.Map;
import me.smiileyface.mcsg.player.GamePlayer;
import me.smiileyface.utils.ChatUtils;
import me.smiileyface.utils.TimeUtils;

public class CountdownPhase {

	private Game game;
	private BukkitTask task;
	private boolean running;
	private int time;
	private Map map;
	
	public CountdownPhase(Game game, Map map) {
		this.game = game;
		this.map = map;
	}
	
	public void load() {
		time = game.getCountdown();
		game.setState(GameState.STARTING);
		start();
	}
	
	public void start() {
		running = true;
		
		for(int i = 0; i < game.getPlayers().size(); i++) {
			GamePlayer player = game.getPlayers().get(i);
			
			player.setSpawnIndex(i);
			player.getPlayer().teleport(game.getMapManager().getSpawns(map).get(i));
			
			for(GamePlayer oplayer : game.getPlayers()) {
				player.getPlayer().showPlayer(oplayer.getPlayer());
			}
		}
		
		task = Bukkit.getScheduler().runTaskTimer(Core.get(), new Runnable() {
			
			@Override
			public void run() {
				if(time == 27) {
					for(GamePlayer player : game.getPlayers()) {
						player.getPlayer().sendMessage(ChatUtils.colorize("&e(Map Info) &a" + map.getName()));
						player.getPlayer().sendMessage(ChatUtils.colorize("&e(Map Info) &a" + map.getAuthor()));
						player.getPlayer().sendMessage(ChatUtils.colorize("&e(Map Info) &a" + map.getLink()));
					}
				}
				
				if(time > 0 && (time % 5 == 0 || (time <= 10 && time > 0))) {
					for(GamePlayer player : game.getPlayers()) {
						player.getPlayer().sendMessage(ChatUtils.modulate("SurvivalGames", TimeUtils.getTime(time, "seconds") + " seconds until the games begin!"));
					}
				}
				
				if(time == 0) {
					task.cancel();
					running = false;
					time = game.getCountdown();
					game.startGame();
					return;
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
		time = game.getCountdown();
	}
	
	public boolean isRunning() {
		return running;
	}
}

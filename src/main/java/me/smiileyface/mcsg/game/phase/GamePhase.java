package me.smiileyface.mcsg.game.phase;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import me.smiileyface.mcsg.Core;
import me.smiileyface.mcsg.game.Game;
import me.smiileyface.mcsg.game.GameState;
import me.smiileyface.mcsg.player.GamePlayer;
import me.smiileyface.utils.ChatUtils;
import me.smiileyface.utils.TimeUtils;

public class GamePhase {

	private Game game;
	private BukkitTask task;
	private boolean running;
	private int time;
	
	private long start;
	private int players;
	
	private BukkitTask deathmatch, chestRefill;
	
	public GamePhase(Game game) {
		this.game = game;
		this.time = game.getGameTime();
	}
	
	public void load() {
		start();
	}
	
	public void start() {
		start = System.currentTimeMillis();
		game.setState(GameState.IN_GAME);
		Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "The games have begun!"));
		running = true;
		players = game.getPlayers().size();
		
		game.getCurrentMap().getMinimumLocation().getWorld().setTime(0);
		
		chestRefill = Bukkit.getScheduler().runTaskLater(Core.get(), new Runnable() {
			
			@Override
			public void run() {
				long time = game.getCurrentMap().getMinimumLocation().getWorld().getTime();
				if(time >= 18000 && time <= 18200) {
					
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "&3These tributes have passed... : &2" + Arrays.asList(game.deadPlayers)));
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "&3Sponsords have refilled &eall &3of the chests!"));
				}
			}
			
		}, 18001);
		
		startTask();
	}
	
	public void startTask() {
		task = Bukkit.getScheduler().runTaskTimer(Core.get(), new Runnable() {
			
			@Override
			public void run() {
				if(time % 600 == 0 && time > 300) {
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", game.getPlayers().size() + " tributes remain!"));
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", TimeUtils.formatTime(time) + " until final deathmatch!"));
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", game.getSpectators().size() + " spectators watching the games."));
				} else if(time <= 300 && time % 60 == 0 && time > 60) {
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", game.getPlayers().size() + " tributes remain!"));
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", TimeUtils.formatTime(time) + " until final deathmatch!"));
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", game.getSpectators().size() + " spectators watching the games."));
				} else if(time <= 60 && time % 10 == 0 && time > 10) {
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", game.getPlayers().size() + " tributes remain!"));
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", TimeUtils.formatTime(time) + " until final deathmatch!"));
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", game.getSpectators().size() + " spectators watching the games."));
				} else if(time <= 10 && time > 0) {
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", TimeUtils.formatTime(time) + " until final deathmatch!"));
				} else if(time == 0) {
					cancelTask();
					game.startDeathmatch();
					return;
				}
				time--;
			}
			
		}, 0L, 20L);
	}
	
	public void killPlayer(GamePlayer player, GamePlayer killer, boolean leave) {
		int remain = game.getPlayers().size() -1;
		int pointsLost = ((int)Math.round(player.getPoints() * 0.2D));
		int pointsGained = pointsLost >= 5 ? pointsLost : 5;
		
		player.addGamePlayed();
		player.addDeath();
		player.addToLifespan(System.currentTimeMillis() - start);
		
		game.addDeadPlayer(player.getRealName());
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ChatUtils.modulate("SurvivalGames", "A cannon could be heard in the distance."));
			p.sendMessage(ChatUtils.modulate("SurvivalGames", remain + " tributes remain."));
		}
		
		if(!leave) {
			if(killer != null) {
				player.getPlayer().sendMessage(ChatUtils.modulate("SurvivalGames", "&3You were elminated from the games!"));
				player.getPlayer().sendMessage(ChatUtils.colorize("&aLost &e" + pointsLost + " &afor being slain by &8(&e" + killer.getPoints() + "&8)" + killer.getRank().getPrefix() + killer.getRealName()));
				player.removePoints(pointsLost);
				
				killer.addKill();
				killer.getPlayer().sendMessage(ChatUtils.colorize("&aAwarded &e" + pointsGained + " &apoints for killing &8(&e" + player.getPoints() + "&8)" + player.getRank().getPrefix() + player.getRealName()));
				killer.addPoints(pointsGained);
				if(player.hasBounty()) {
					killer.addPoints(player.getBounty());
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "&8(&e" + killer.getPoints() + "&8)" + killer.getChatPrefix() + killer.getRealName() + " &3has collected the &e" + player.getBounty() + " &3point bounty on &8(&e" + player.getPoints() + "&8)" + player.getChatPrefix() + player.getRealName() + "&e's head."));
					player.resetBounty();
				}
			}
		}
		
		player.getPlayer().getWorld().strikeLightningEffect(player.getPlayer().getLocation());
		
		for(ItemStack is : player.getPlayer().getInventory().getContents()) {
			if(is == null || is.getType() == Material.AIR)
				continue;
			player.getPlayer().getWorld().dropItemNaturally(player.getPlayer().getLocation(), is);
		}
		
		for(ItemStack is : player.getPlayer().getInventory().getArmorContents()) {
			if(is == null || is.getType() == Material.AIR)
				continue;
			player.getPlayer().getWorld().dropItemNaturally(player.getPlayer().getLocation(), is);
		}
		final Player p = player.getPlayer();
		
		game.getPlayers().remove(player);
		game.getSpectators().add(player);
		player.clear();
		
		if(p.getVehicle() != null)
			p.getVehicle().setPassenger(null);
		if(p.getPassenger() != null) {
			p.setPassenger(null);
		}
		
		if(remain == 1) {
			GamePlayer winner = game.getPlayers().get(0);
			
			winner.addWin();
			winner.addGamePlayed();
			
			Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "Tribute " + winner.getRealName() + " has won!"));
			
			game.end();
		} else {
			if(remain == 3) {
				startDeathmatchTask();
			}
		}
	}
	
	public void startDeathmatchTask() {
		cancelTask();
		
		deathmatch = Bukkit.getScheduler().runTaskTimer(Core.get(), new Runnable() {
			int time = 60;
			@Override
			public void run() {
				if(time % 10 == 0 && time > 10) {
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "&3There are " + time + " seconds left until deathmatch."));
				} else if(time <= 10 && time > 0) {
					Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "&3There are " + time + " seconds left until deathmatch."));
				} else if(time == 0) {
					cancelDeathmatchTask();
					game.startDeathmatch();
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
		if(chestRefill != null)
			chestRefill.cancel();
	}
	
	public void cancelDeathmatchTask() {
		if(deathmatch != null)
			deathmatch.cancel();
	}
	
	public boolean isRunning() {
		return running;
	}
}

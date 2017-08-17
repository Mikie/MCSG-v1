package me.smiileyface.mcsg.game.phase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.smiileyface.mcsg.Core;
import me.smiileyface.mcsg.game.Game;
import me.smiileyface.mcsg.game.GameState;
import me.smiileyface.mcsg.map.Map;
import me.smiileyface.mcsg.player.GamePlayer;
import me.smiileyface.utils.ChatUtils;
import me.smiileyface.utils.TimeUtils;

public class LobbyPhase {

	private Game game;
	private BukkitTask task;
	private boolean running = false;
	private int time;
	
	public ArrayList<Map> voteMaps = new ArrayList<Map>();
	
	public LobbyPhase(Game game) {
		this.game = game;
	}
	
	public void load() {
		game.setState(GameState.LOBBY);
		chooseRandomMaps();
		time = game.getLobbyTime();
		start();
	}
	
	public void start() {
		running = true;
		
		task = Bukkit.getScheduler().runTaskTimer(Core.get(), new Runnable() {
			
			@Override
			public void run() {
				if(time % 30 == 0 && time != 0) {
					sendVoteMessage();
				} else if(time == 0) {
					task.cancel();
					running = false;
					time = game.getLobbyTime();
					Map winner  = getMostVotedMap();
					game.getMapManager().loadMap(winner);
					game.getMapManager().getSpawns(winner).get(0).getWorld().setTime(0);
					
					game.setCurrentMap(winner);
					game.startCountdown();
					for(Map map : voteMaps) {
						map.setVotes(0);
					}
					voteMaps.clear();
					game.getVoted().clear();
					return;
				}
				
				time--;
			}
			
		}, 0L, 20L);
	}
	
	public List<Map> getMaps() {
		return voteMaps;
	}
	
	public Map getMostVotedMap() {
		Map mostVoted = null;
		
		int votes = 0;
		for(Map map : voteMaps) {
			if(map.getVotes() > votes) {
				votes = map.getVotes();
				mostVoted = map;
			}
		}
		
		if(mostVoted == null)
			mostVoted = voteMaps.get(new Random().nextInt(voteMaps.size()));
		return mostVoted;
	}
	
	public boolean canVote(GamePlayer player) {
		return !game.getVoted().contains(player);
	}
	
	public Map vote(GamePlayer p, int id) {
		try {
			Map m = voteMaps.get(id - 1);
			if(m != null) {
				int amount = p.getVoteAmount();
				m.setVotes(m.getVotes() + amount);
				game.getVoted().add(p);
				p.getPlayer().sendMessage(ChatUtils.modulate("SurvivalGames", "You voted for " + m.getName()));
			}
			
			return m;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	private void sendVoteMessage() {
		if(game.isVoting()) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.sendMessage(ChatUtils.modulate("SurvivalGames", "There are " + game.getPlayers().size() + "/" + game.getMaxPlayers() + " tributes waiting to play."));
				player.sendMessage(ChatUtils.modulate("SurvivalGames", "Games require at least " + game.getRequiredPlayers() + " tributes."));
				player.sendMessage(ChatUtils.modulate("SurvivalGames", "Time remaining: &b" + TimeUtils.formatTime(time)));
				player.sendMessage(ChatUtils.modulate("SurvivalGames", "Vote for maps using /vote #."));
				player.sendMessage(ChatUtils.modulate("SurvivalGames", "Want different options? Use /skip"));
				int i = 1;
				for(Map map : voteMaps) {
					player.sendMessage(ChatUtils.modulate("SurvivalGames", i + " : " + map.getName() + " (" + map.getAuthor() + ") (" + map.getVotes() + ")"));
					i++;
				}
			}
		}
	}
	
	private void chooseRandomMaps() {
		List<Map> maps = game.getMapManager().getMaps();
		
		voteMaps.clear();
		Collections.shuffle(maps);
		
		int i = 0;
		for(Map m : maps) {
			if(i == game.getMaxVotingMaps())
				break;
			voteMaps.add(m);
			i++;
		}
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public int getTime() {
		return time;
	}
	
	public void cancelTask() {
		if(task != null)
			task.cancel();
		running = false;
		voteMaps.clear();
		time = game.getLobbyTime();
		return;
	}
}

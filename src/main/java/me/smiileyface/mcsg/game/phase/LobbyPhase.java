package me.smiileyface.mcsg.game.phase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.smiileyface.mcsg.Core;
import me.smiileyface.mcsg.game.Game;
import me.smiileyface.mcsg.game.GameState;
import me.smiileyface.mcsg.game.VoteMap;
import me.smiileyface.mcsg.player.GamePlayer;
import me.smiileyface.utils.ChatUtils;
import me.smiileyface.utils.TimeUtils;

public class LobbyPhase {

	private Game game;
	private BukkitTask task;
	private boolean running = false;
	private int time;
	
	public int mapAmount = 0;
	public ArrayList<Integer> maps = new ArrayList<Integer>();
	public HashMap<Integer, VoteMap> voteMaps = new HashMap<Integer, VoteMap>();
	
	public LobbyPhase(Game game) {
		this.game = game;
	}
	
	public void load() {
		game.setState(GameState.LOBBY);
		for(int i = 0; i < 999; i++) {
			if(Core.getMaps().contains("" + i)) {
				mapAmount = i;
				break;
			}
		}
		selectMaps();
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
					VoteMap winner  = getMostVotedMap();
					game.loadMap(winner);
					
					game.setCurrentMap(winner.mapID);
					game.startCountdown();

					maps.clear();
					game.getVoted().clear();
					return;
				}
				
				time--;
			}
			
		}, 0L, 20L);
	}
	
	public HashMap<Integer, VoteMap> getMaps() {
		return voteMaps;
	}
	
	public VoteMap getMostVotedMap() {
		VoteMap mostVoted = new VoteMap(999);
	
		for(VoteMap m : voteMaps.values()) {
			if((mostVoted.mapID == 999) || (m.votes > mostVoted.votes)) {
				mostVoted = m;
			}
		}
		
		if(mostVoted.mapID == 999)
			mostVoted = voteMaps.get(new Random().nextInt(voteMaps.size()));
		return mostVoted;
	}
	
	public boolean canVote(GamePlayer player) {
		return !game.getVoted().contains(player);
	}
	
	public VoteMap vote(GamePlayer p, int id) {
		try {
			VoteMap m = voteMaps.get(id - 1);
			if(m != null) {
				int amount = p.getVoteAmount();
				m.votes = m.votes + amount;
				game.getVoted().add(p);
				p.getPlayer().sendMessage(ChatUtils.modulate("SurvivalGames", "You voted for " + m.mapName));
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
				for(VoteMap map : voteMaps.values()) {
					player.sendMessage(ChatUtils.modulate("SurvivalGames", i + " : " + map.mapName +  "&8(&e" + map.votes + "&8)"));
					i++;
				}
			}
		}
	}
	
	private void chooseRandomMaps() {
		if(!maps.isEmpty()) {
			if(mapAmount > game.getMaxVotingMaps()) {
				Random r = new Random();
				for(int i = 0; i < game.getMaxVotingMaps(); i++) {
					int ran = r.nextInt(mapAmount);
					int newRan = 0;
					while(maps.contains(Integer.valueOf(ran))) {
						newRan = r.nextInt(mapAmount);
						if(!maps.contains(Integer.valueOf(newRan))) {
							ran = newRan;
						}
					}
					maps.add(Integer.valueOf(ran));
					
					voteMaps.put(i, new VoteMap(ran));
				}
			} else {
				for(int i = 0; i < mapAmount; i++) {
					if(Core.getMaps().contains("" + i)) {
						maps.add(Integer.valueOf(i));
						voteMaps.put(i, new VoteMap(i));
					}
				}
			}
		}
	}
	
	public void selectMaps() {
		voteMaps.clear();
		for(int i = 0; i < 999; i++) {
			if(Core.getMaps().contains(String.valueOf(i))) {
				voteMaps.put(i, new VoteMap(i));
			}
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

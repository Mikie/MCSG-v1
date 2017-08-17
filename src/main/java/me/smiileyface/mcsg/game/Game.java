package me.smiileyface.mcsg.game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.smiileyface.mcsg.Core;
import me.smiileyface.mcsg.event.GameStateChangeEvent;
import me.smiileyface.mcsg.game.phase.CountdownPhase;
import me.smiileyface.mcsg.game.phase.DeathmatchPhase;
import me.smiileyface.mcsg.game.phase.EndPhase;
import me.smiileyface.mcsg.game.phase.GamePhase;
import me.smiileyface.mcsg.game.phase.LobbyPhase;
import me.smiileyface.mcsg.player.GamePlayer;
import me.smiileyface.utils.ChatUtils;
import me.smiileyface.utils.ZipUtils;

public class Game {

	private boolean voting;
	private int maxVotingMaps;
	private int reqPlayers, maxPlayers;
	private GameState state;
	private int lobbyTime = 60 * 3;
	private int gameTime = 60 * 40;
	private int deathmatchTime = 60 * 5;
	private int countdown = 30;

	private LobbyPhase lobbyPhase;
	private CountdownPhase countdownPhase;
	private GamePhase gamePhase;
	private DeathmatchPhase deathmatchPhase;
	private EndPhase endPhase;

	private int map;
	private Location lobbySpawn;
	private List<GamePlayer> players = new ArrayList<GamePlayer>();
	private List<GamePlayer> spectators = new ArrayList<GamePlayer>();
	public ArrayList<GamePlayer> voted = new ArrayList<GamePlayer>();

	public ArrayList<String> deadPlayers = new ArrayList<String>();

	private boolean forcedStart = false;

	public Game(boolean voting, int lobbyTime, int maxVotingMaps, int reqPlayers) {
		this.voting = voting;
		this.lobbyTime = lobbyTime;
		this.maxVotingMaps = maxVotingMaps;

		if (reqPlayers < 2)
			reqPlayers = 2;

		this.reqPlayers = reqPlayers;
		this.maxPlayers = 24;

		

		setState(GameState.LOBBY);
	}

	public void forceStart(Player p) {
		if (players.size() < 2) {
			p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cAt least 2 players are required to start the game!"));
			return;
		}

		if (getState() != GameState.LOBBY) {
			p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cThe game is already starting!"));
			return;
		}

		forcedStart = true;
		startGame();
		p.sendMessage(ChatUtils.modulate("SurvivalGames", "You've started the game successfully"));
	}

	public void startGame() {
		gamePhase = new GamePhase(this);
		gamePhase.load();
	}

	public void startCountdown() {
		countdownPhase = new CountdownPhase(this, map);
		countdownPhase.load();
	}

	public void checkForStart() {
		if (players.size() == reqPlayers || forcedStart) {
			if (countdownPhase != null && countdownPhase.isRunning())
				return;

			lobbyPhase = new LobbyPhase(this);
			lobbyPhase.load();
			if (forcedStart) {
				if (getLobbyTime() > 30)
					setLobbyTime(30);
			}
		}
	}

	public void checkCancelStart() {
		if (state != GameState.LOBBY && state != GameState.STARTING)
			return;

		if (forcedStart) {
			if (players.size() == 1) {
				if (getState() == GameState.STARTING) {
					countdownPhase.cancelTask();
					Bukkit.broadcastMessage(
							ChatUtils.modulate("SurvivalGames", "Not enough players to start, cancelling game."));
				} else if (getState() == GameState.LOBBY && lobbyPhase.isRunning()) {
					lobbyPhase.cancelTask();
					Bukkit.broadcastMessage(
							ChatUtils.modulate("SurvivalGames", "Not enough players to start, resetting countdown."));
				}
				forcedStart = false;
			}
		} else {
			if (players.size() == reqPlayers - 1) {
				if (getState() == GameState.STARTING) {
					countdownPhase.cancelTask();
					Bukkit.broadcastMessage(
							ChatUtils.modulate("SurvivalGames", "Not enough players to start, cancelling game."));
				} else if (getState() == GameState.LOBBY && lobbyPhase.isRunning()) {
					lobbyPhase.cancelTask();
					Bukkit.broadcastMessage(
							ChatUtils.modulate("SurvivalGames", "Not enough players to start, resetting countdown."));
				}
			}
		}
	}

	public void end() {
		endPhase = new EndPhase(this);
		endPhase.load();
	}

	public void startDeathmatch() {
		deathmatchPhase = new DeathmatchPhase(this);
		deathmatchPhase.load();
	}

	public LobbyPhase getLobbyPhase() {
		return lobbyPhase;
	}

	public CountdownPhase getCountdownPhase() {
		return countdownPhase;
	}

	public GamePhase getGamePhase() {
		return gamePhase;
	}

	public DeathmatchPhase getDeathmatchPhase() {
		return deathmatchPhase;
	}

	public EndPhase getEndPhase() {
		return endPhase;
	}

	public boolean isVoting() {
		return voting;
	}

	public int getMaxVotingMaps() {
		return maxVotingMaps;
	}

	public int getRequiredPlayers() {
		return reqPlayers;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;

		GameStateChangeEvent e = new GameStateChangeEvent(state);
		Bukkit.getServer().getPluginManager().callEvent(e);
	}

	public int getLobbyTime() {
		return lobbyTime;
	}

	public void setLobbyTime(int time) {
		this.lobbyTime = time;
	}

	public int getCountdown() {
		return countdown;
	}

	public void setCountdown(int time) {
		this.countdown = time;
	}

	public int getGameTime() {
		return gameTime;
	}

	public void setGameTime(int time) {
		this.gameTime = time;
	}

	public int getDeathmatchTime() {
		return deathmatchTime;
	}

	public void setDeathmatchTime(int time) {
		this.deathmatchTime = time;
	}

	public int getCurrentMap() {
		return map;
	}

	public void setCurrentMap(int map) {
		this.map = map;
	}
	
	public String serializeLocation(Location l) {
		return l.getWorld().getName() + "=" + l.getX() + "=" + l.getY() + "=" + l.getZ() + "=" + l.getPitch() + "="
				+ l.getYaw();
	}
	
	public Location unserializeLocation(String l) {
		String[] split = l.split("=");
		Location loc = new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]),
				Double.parseDouble(split[2]), Double.parseDouble(split[2]));
		loc.setPitch(Float.parseFloat(split[4]));
		loc.setYaw(Float.parseFloat(split[5]));
		return loc;
	}
	
	public void joinSpawns(List<GamePlayer> players) {
		List<String> spawns = Core.getMaps().getList(map + ".spawns");
		for (int i = 0; i < players.size(); i++) {
			if (i < Bukkit.getOnlinePlayers().size()) {
				String[] spawn = ((String) spawns.get(i)).split("=");

				Location l = new Location(Bukkit.getWorld(spawn[0]), Double.parseDouble(spawn[1]),
						Double.parseDouble(spawn[2]), Double.parseDouble(spawn[3]));
				l.setPitch(Float.parseFloat(spawn[4]));
				l.setYaw(Float.parseFloat(spawn[5]));
				l.getChunk().load();
				players.get(i).getPlayer().teleport(l);
			}
		}
	}
	
	public void joinDMSpawns(List<GamePlayer> players) {
		List<String> spawns = Core.getMaps().getList(map + ".deathmatch-spawns");
		for (int i = 0; i < players.size(); i++) {
			if (i < Bukkit.getOnlinePlayers().size()) {
				String[] spawn = ((String) spawns.get(i)).split("=");

				Location l = new Location(Bukkit.getWorld(spawn[0]), Double.parseDouble(spawn[1]),
						Double.parseDouble(spawn[2]), Double.parseDouble(spawn[3]));
				l.setPitch(Float.parseFloat(spawn[4]));
				l.setYaw(Float.parseFloat(spawn[5]));
				l.getChunk().load();
				players.get(i).getPlayer().teleport(l);
			}
		}
	}
	
	public Location getLobbySpawn() {
		String[] lsplit = Core.getConfigFile().getString("lobby-spawn").split("=");

		this.lobbySpawn = new Location(Bukkit.getWorld(lsplit[0]), Double.parseDouble(lsplit[1]),
				Double.parseDouble(lsplit[2]), Double.parseDouble(lsplit[3]));
		this.lobbySpawn.setPitch(Float.parseFloat(lsplit[4]));
		this.lobbySpawn.setYaw(Float.parseFloat(lsplit[5]));

		return this.lobbySpawn;
	}

	public void loadMap(VoteMap map) {
		try {
			ZipUtils.extractZIP(new File(Core.get().getDataFolder() + "/maps/" + map.mapName + ".zip"), new File(Core.getMaps().getString(map.mapName)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<GamePlayer> getPlayers() {
		return players;
	}

	public void addPlayer(GamePlayer player) {
		this.players.add(player);
	}

	public List<GamePlayer> getSpectators() {
		return spectators;
	}

	public void addSpectator(GamePlayer player) {
		this.spectators.add(player);
	}

	public ArrayList<GamePlayer> getVoted() {
		return voted;
	}

	public void addVoted(GamePlayer player) {
		this.voted.add(player);
	}

	public boolean hasPlayerIndex(int index) {
		for (GamePlayer player : players) {
			if (player.getSpawnIndex() == index)
				return true;
		}

		return false;
	}

	public ArrayList<String> getDeadPlayers() {
		return deadPlayers;
	}

	public void addDeadPlayer(String player) {
		this.deadPlayers.add(player);
	}
}

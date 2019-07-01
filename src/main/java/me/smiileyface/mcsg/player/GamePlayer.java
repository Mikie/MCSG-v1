package me.smiileyface.mcsg.player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.smiileyface.backend.Players;
import me.smiileyface.backend.Stats;
import me.smiileyface.backend.players.Rank;

public class GamePlayer {

	private static HashMap<String, GamePlayer> players = new HashMap<String, GamePlayer>();
	public HashMap<String, GamePlayer> getPlayers() {
		return players;
	}
	
	public static GamePlayer getPlayer(String name) {
		if(!players.containsKey(name))
			return null;
		return players.get(name);
	}
	
	public static GamePlayer createPlayer(Player p) {
		if(!players.containsKey(p.getName())) {
			GamePlayer gp = Players.getData().getPlayerFromUUID(p);
			if(gp == null) {
				try {
					gp = Players.getData().getPlayerFromName(p);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			} else {
				Players.getData().updateNameFromUUID(p);
			}
			
			if(gp == null && p != null) {
				gp = new GamePlayer(p);
			}
			
			players.put(p.getDisplayName(), gp);
			return gp;
		} else {
			return getPlayer(p.getName());
		}
	}
	
	public static void removePlayer(String name) {
		if(players.containsKey(name)) {
			players.remove(name);
		}
	}
	
	private Player player;
	private String realName;
	private UUID uuid;
	private String nickname;
	
	private boolean inSpecChat;
	
	private Rank rank;
	private long rankExpiry;
	
	private int wins, losses, gamesPlayed, kills, deaths, points;
	private long lifespan;
	
	private int spawnIndex;
	
	private int specIndex = 0;
	
	private int bounty;
	
	public GamePlayer(Player p) {
		this.player = p;
		this.realName = p.getName();
		this.nickname = p.getName();
		this.uuid = p.getUniqueId();
		this.inSpecChat = false;
		this.rank = Rank.REGULAR;
		this.rankExpiry = 0;
		this.wins = 0;
		this.losses = 0;
		this.gamesPlayed = 0;
		this.kills = 0;
		this.deaths = 0;
		this.points = 0;
		this.lifespan = 0;
	}
	
	public GamePlayer(Player p, int rank, long rankExpiry) {
		this.player = p;
		this.realName = p.getName();
		this.nickname = p.getName();
		this.uuid = p.getUniqueId();
		this.inSpecChat = false;
		this.rank = Players.getRanks().getRankFromID(rank);
		this.rankExpiry = rankExpiry;
		this.wins = Stats.getWins().getWins(p);
		this.losses = Stats.getLosses().getLosses(p);
		this.gamesPlayed = Stats.getGamesPlayed().getGamesPlayed(p);
		this.kills = Stats.getKills().getKills(p);
		this.deaths = Stats.getDeaths().getDeaths(p);
		this.points = Stats.getPoints().getPoints(p);
		this.lifespan = Stats.getLifespan().getLifespan(p);
	}
	
	public int getVoteAmount() {
		switch(getRank()) {
			case REGULAR:
				return 1;
			case IRON:
				return 2;
			case GOLD:
				return 3;
			default:
				return 4;
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public String getRealName() {
		return realName;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	// TODO: setNickname()
	
	public boolean isNicknamed() {
		return !getRealName().equals(getNickname());
	}
	
	public boolean isInSpecChat() {
		return inSpecChat;
	}
	
	public void setInSpecChat(boolean specChat) {
		this.inSpecChat = specChat;
	}
	
	public String getChatPrefix() {
		return getRank().getPrefix();
	}
	
	public Rank getRank() {
		return rank;
	}
	
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	public long getRankExpiry() {
		return rankExpiry;
	}
	
	public void setRankExpiry(long expiry) {
		this.rankExpiry = expiry;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void addPoints(int points) {
		Stats.getPoints().setPoints(uuid, Stats.getPoints().getPoints(player) + points);
	}
	
	public void removePoints(int points) {
		Stats.getPoints().setPoints(uuid, Stats.getPoints().getPoints(player) - points);
	}
	
	public void setPoints(int points) {
		Stats.getPoints().setPoints(uuid, points);
	}
	
	public int getWins() {
		return wins;
	}
	
	public void addWin() {
		Stats.getWins().setWins(uuid, Stats.getWins().getWins(player) + 1);
	}
	
	public int getLosses() {
		return losses;
	}
	
	public void addLoss() {
		Stats.getLosses().setLosses(uuid, Stats.getLosses().getLosses(player) + 1);
	}
	
	public int getGamesPlayed() {
		return gamesPlayed;
	}
	
	public void addGamePlayed() {
		Stats.getGamesPlayed().setGamesPlayed(uuid, Stats.getGamesPlayed().getGamesPlayed(player) + 1);
	}
	
	public int getKills() {
		return kills;
	}
	
	public void addKill() {
		Stats.getKills().setKills(uuid, Stats.getKills().getKills(player) + 1);
	}
	
	public int getDeaths() {
		return deaths;
	}
	
	public void addDeath() {
		Stats.getDeaths().setDeaths(uuid, Stats.getDeaths().getDeaths(player) + 1);
	}
	
	public long getLifespan() {
		return lifespan;
	}
	
	public void addToLifespan(long lifespan) {
		Stats.getLifespan().setLifespan(uuid, Stats.getLifespan().getLifespan(player) + lifespan);
	}
	
	public String getKDR() {
		return new DecimalFormat("#.##").format("" + Stats.getKills().getKills(player) / Stats.getDeaths().getDeaths(player));
	}
	
	public int getSpawnIndex() {
		return spawnIndex;
	}
	
	public void setSpawnIndex(int index) {
		this.spawnIndex = index;
	}
	
	public int getSpecIndex() {
		return specIndex;
	}
	
	public void setSpecIndex(int index) {
		this.specIndex = index;
	}
	
	public int getBounty() {
		return bounty;
	}
	
	public void addBounty(int amount) {
		this.bounty = this.bounty + amount;
	}
	
	public void resetBounty() {
		this.bounty = 0;
	}
	
	public boolean hasBounty() {
		return bounty != 0;
	}
	
	public void clear() {
		for (Iterator<PotionEffect> i = player.getActivePotionEffects().iterator(); i.hasNext();) {
			player.removePotionEffect(i.next().getType());
		}
		player.setWalkSpeed(0.2F);
		player.setFlySpeed(0.1F);
		player.setMaxHealth(20D);
		player.setHealth(20D);
		player.setFoodLevel(20);
		player.setLevel(0);
		player.setExp(0);
		player.setFireTicks(0);
		player.setGameMode(GameMode.SURVIVAL);
		player.setFlying(false);
		player.setAllowFlight(false);
		player.getInventory().setHeldItemSlot(0);

		clearInventory();
	}
	
	public void clearInventory() {
		ItemStack[] inv = player.getInventory().getContents();
		for (int i = 0; i < inv.length; i++) {
			inv[i] = null;
		}
		player.getInventory().setContents(inv);
		inv = player.getInventory().getArmorContents();
		for (int i = 0; i < inv.length; i++) {
			inv[i] = null;
		}
		player.getInventory().setArmorContents(inv);
		player.updateInventory();
	}
} 

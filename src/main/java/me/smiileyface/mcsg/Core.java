package me.smiileyface.mcsg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.smiileyface.backend.Servers;
import me.smiileyface.backend.database.DBConnection;
import me.smiileyface.mcsg.command.admin.GameCommands;
import me.smiileyface.mcsg.command.user.Vote;
import me.smiileyface.mcsg.event.GameStateChangeEvent;
import me.smiileyface.mcsg.game.Chests;
import me.smiileyface.mcsg.game.Game;
import me.smiileyface.mcsg.listener.ChatListener;
import me.smiileyface.mcsg.listener.PlayerListener;
import me.smiileyface.mcsg.listener.SpectatorListener;

public class Core extends JavaPlugin implements Listener {

	private final static String SERVERS_DATABASE = "CREATE TABLE IF NOT EXISTS servers (nickname VARCHAR(40), ip VARCHAR(40), port INT, lastchange VARCHAR(40), gamestate INT, mapname VARCHAR(40), players INT);";
	private final static String PLAYERS_DATABASE = "CREATE TABLE IF NOT EXISTS players (id INT NOT NULL AUTO_INCREMENT, username VARCHAR(40), uuid VARCHAR(256), `rank` INT, rankExpiry BIGINT, lastip VARCHAR(40), PRIMARY KEY (id));";
	private final static String STATS_DATABASE = "CREATE TABLE IF NOT EXISTS stats (username VARCHAR(40), uuid VARCHAR(256), wins INT, losses INT, gamesplayed INT, kills INT, deaths INT, points INT, lifespan BIGINT, chestsOpened INT);";
	private final static String PUNISHMENTS_DATABASE = "CREATE TABLE IF NOT EXISTS punishments (id INT NOT NULL AUTO_INCREMENT, punished VARCHAR(256), ipAddress VARCHAR(40), issuer VARCHAR(256), type VARCHAR(40), reason VARCHAR(256), time VARCHAR(40), end BIGINT, active TINYINT(1), PRIMARY KEY (id));";

	private static Core instance;

	public static Core get() {
		return instance;
	}

	private static SettingsManager c;
	private static SettingsManager ch;
	private static SettingsManager m;

	private Game game;

	@Override
	public void onEnable() {
		instance = this;

		c = new SettingsManager("config");
		ch = new SettingsManager("chests");
		m = new SettingsManager("maps");
		
		writeToConfig();
		writeToChests();
		writeToMaps();
		
		getCommand("sg").setExecutor(new GameCommands());
		getCommand("vote").setExecutor(new Vote());
		
		game = new Game(true, 60 * 3, 4, 2);

		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new ChatListener(), this);
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(new SpectatorListener(), this);
		pm.registerEvents(new Chests(), this);

		Chests.c.setTiers();

		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement servers = c.prepareStatement(SERVERS_DATABASE);
			PreparedStatement players = c.prepareStatement(PLAYERS_DATABASE);
			PreparedStatement stats = c.prepareStatement(STATS_DATABASE);
			PreparedStatement punishments = c.prepareStatement(PUNISHMENTS_DATABASE);
			servers.executeUpdate();
			servers.close();
			players.executeUpdate();
			players.close();
			stats.executeUpdate();
			stats.close();
			punishments.executeUpdate();
			punishments.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (Servers.getData().isInDatabase()) {
			Servers.getData().updateAllInfo();
		} else {
			Servers.getData().addToDatabase();
		}
	}

	private void writeToConfig() {
		c.createPath("SQL", "");
		c.createPath("SQL.host", "localhost");
		c.createPath("SQL.database", "mcsg");
		c.createPath("SQL.username", "root");
		c.createPath("SQL.password", "Minecraft1337");
		c.createPath("serverName", "US1");
		c.createPath("lobby-spawn", "world=0=75=0=0=0");
		c.save();
	}

	private void writeToChests() {
		ch.createPath("max-items", Integer.valueOf(8));
		ch.createPath("max-items-doublechest", Integer.valueOf(16));
		ch.createPath("rarer-tier2", Boolean.valueOf(true));
		ch.createPath("restock-time", Integer.valueOf(600));
		ch.createStringList("tier1",
				new String[] { "WOOD_SWORD", "STONE_AXE", "GOLD_SWORD", "GOLD_AXE", "WOOD_AXE", "FISHING_ROD", "BOW",
						"ARROW", "ARROW, 3", "ARROW, 5", "COOKED_FISH", "RAW_FISH, 2", "APPLE, 3", "LEATHER_HELMET",
						"LEATHER_CHESTPLATE", "LEATHER_LEGGINGS", "LEATHER_BOOTS", "GOLD_INGOT", "STICK" });

		ch.createStringList("tier2",
				new String[] { "STONE_SWORD", "IRON_AXE", "IRON_HELMET", "IRON_CHESTPLATE", "IRON_LEGGINGS",
						"IRON_BOOTS", "CHAINMAIL_HELMET", "CHAINMAIL_CHESTPLATE", "CHAINMAIL_LEGGINGS",
						"CHAINMAIL_BOOTS", "GOLD_HELMET", "GOLD_CHESTPLATE", "GOLD_LEGGINGS", "GOLD_BOOTS", "ARROW, 5",
						"ARROW, 7", "FLINT_AND_STEEL", "DIAMOND", "IRON_INGOT", "GOLD_INGOT, 2", "STICK, 2",
						"GOLDEN_APPLE", "GOLDEN_CARROT", "BAKED_POTATO", "COOKED_BEEF", "COOKED_CHICKEN" });
		ch.save();
	}

	private void writeToMaps() {
		m.createSection("1");
		m.createPath("1.name", "Example Map");
		m.createStringList("1.spawns", "1=1=1=1=1", "2=2=2=2=2", "3=3=3=3=3", "4=4=4=4=4");
        m.createStringList("1.deathmatch-spawns", "1=1=1=1=1", "2=2=2=2=2", "3=3=3=3=3", "4=4=4=4=4");
        m.createStringList("1.tier2", "1=1=1", "2=2=2", "3=3=3", "4=4=4");
        m.save();
    }

	public static SettingsManager getConfigFile() {
		return c;
	}
	
	public static SettingsManager getMaps() {
		return m;
	}
	
	public static SettingsManager getChests() {
		return ch;
	}
	
	public Game getGame() {
		return game;
	}

	@EventHandler
	public void onStateChange(GameStateChangeEvent e) {
		Servers.getData().updateAllInfo();
	}
}

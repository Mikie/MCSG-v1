package me.smiileyface.mcsg;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.smiileyface.backend.Servers;
import me.smiileyface.backend.database.DBConnection;
import me.smiileyface.mcsg.event.GameStateChangeEvent;
import me.smiileyface.mcsg.game.Game;
import me.smiileyface.mcsg.listener.ChatListener;
import me.smiileyface.mcsg.listener.PlayerListener;
import me.smiileyface.mcsg.listener.SpectatorListener;
import me.smiileyface.mcsg.map.Lobby;
import me.smiileyface.mcsg.map.Map;
import me.smiileyface.mcsg.map.MapManager;
import me.smiileyface.utils.StringUtils;

public class Core extends JavaPlugin implements Listener {

	private final static String SERVERS_DATABASE = "CREATE TABLE IF NOT EXISTS servers (nickname VARCHAR(40), ip VARCHAR(40), port INT, lastchange VARCHAR(40), gamestate INT, mapname VARCHAR(40), players INT);";
	private final static String PLAYERS_DATABASE = "CREATE TABLE IF NOT EXISTS players (id INT NOT NULL AUTO_INCREMENT, username VARCHAR(40), uuid VARCHAR(256), rank INT, rankExpiry LONG, lastip VARCHAR(40), PRIMARY KEY (id));";
	private final static String STATS_DATABASE = "CREATE TABLE IF NOT EXISTS stats (username VARCHAR(40), uuid VARCHAR(256), wins INT, losses INT, gamesplayed INT, kills INT, deaths INT, points INT, lifespan LONG, chestsOpened INT);";
	private final static String PUNISHMENTS_DATABASE = "CREATE TABLE IF NOT EXISTS punishments (id INT NOT NULL, punished VARCHAR(256), ipAddress VARCHAR(40), issuer VARCHAR(256), type VARCHAR(40), reason VARCHAR(256), time VARCHAR(40), end LONG, active BOOL, PRIMARY KEY (id));";
	
	private static Core instance;
	public static Core get() {
		return instance;
	}
	
	private Game game;
	private MapManager mapManager;
	
	public static FileConfiguration maps;
	
	@Override
	public void onEnable() {
		instance = this;
		
		mapManager = new MapManager();
		
		maps = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "maps.yml"));
		
		getConfig().addDefault("lobby.name", "Lobby");
		getConfig().addDefault("lobby.world", "world");
		getConfig().addDefault("lobby.spawn-location", "0,100,0,0,0");
		
		saveMaps();
		saveConfig();
		
		mapManager.setupLobby(new Lobby(getConfig().getString("lobby.name"), getConfig().getString("lobby.world")));
		mapManager.getLobby().getWorld();
		for(String map : maps.getConfigurationSection("maps").getKeys(false)) {
			Map m = new Map(StringUtils.strToLoc(Bukkit.getWorld(maps.getString("maps." + map + ".name")), maps.getString("maps." + map + ".min")), 
					StringUtils.strToLoc(Bukkit.getWorld(maps.getString("maps." + map + ".name")), maps.getString("maps." + map + ".max")), 
					maps.getString("maps." + map + ".name"), 
					maps.getString("maps." + map + ".author"), 
					maps.getString("maps." + map + ".link"), 
					StringUtils.strToLoc(Bukkit.getWorld(maps.getString("maps." + map + ".name")), maps.getString("maps." + map + ".middle")), 
					maps.getInt("maps." + map + ".radius"));
			getMapManager().setupMap(m);
		}
		
		game = new Game(mapManager, true, 60*3, 4, 2);
		
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new ChatListener(), this);
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(new SpectatorListener(), this);
		
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
		
		if(Servers.getData().isInDatabase()) {
			Servers.getData().updateAllInfo();
		} else {
			Servers.getData().addToDatabase();
		}
	}
	
	public MapManager getMapManager() {
		return mapManager;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void saveMaps() {
		try  {
			maps.save("plugins/MCSG/maps.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveConfig() {
		try  {
			getConfig().save("plugins/MCSG/config.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onStateChange(GameStateChangeEvent e) {
		Servers.getData().updateAllInfo();
	}
}

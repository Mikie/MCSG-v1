package me.smiileyface.backend.servers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import me.smiileyface.backend.Servers;
import me.smiileyface.backend.database.DBConnection;
import me.smiileyface.mcsg.Core;
import me.smiileyface.mcsg.game.GameState;

public class ServerData {

	private String serverName;
	
	public synchronized boolean isInDatabase() {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT * FROM servers WHERE nickname=?");
			ps.setString(1, getName().toLowerCase());
			ResultSet rs = ps.executeQuery();
			boolean has = rs.next();
			if(has) {
				rs.close();
				ps.close();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public synchronized void addToDatabase() {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			c.createStatement().execute("INSERT INTO servers (nickname, ip, port, lastchange, gamestate, mapname, players) VALUES('" 
			+ getName() + "', '"
			+ Bukkit.getIp() + "', "
			+ Bukkit.getPort() + ", '"
			+ System.currentTimeMillis() / 1000 + "', "
			+ Servers.getState().getNumberFromState(Core.get().getGame().getState()) + ", '"
			+ (Core.get().getGame().getState() == GameState.LOBBY ? Core.get().getMapManager().getLobby().getName() : Core.get().getGame().getCurrentMap().getName()) + "', "
			+ Core.get().getGame().getPlayers().size()
			+ ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		if(serverName == null) {
			serverName = Core.get().getConfig().getString("serverName");
		}
		return serverName;
	}
	
	public boolean isDevServer() {
		return getName().toLowerCase().contains("dev");
	}
	
	public synchronized String getNameFromDB() {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT nickname FROM servers WHERE ip=? AND port=?");
			ps.setString(1, Bukkit.getIp());
			ps.setInt(2, Bukkit.getPort());
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				return rs.getString("nickname");
			} else {
				rs.close();
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Unknown";
	}
	
	public synchronized boolean isInactive(String server) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT lastchange FROM servers WHERE nickname=?");
			ps.setString(1, server);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return Integer.parseInt(rs.getString("lastchange")) < (System.currentTimeMillis() / 1000) - 60;
			} else {
				rs.close();
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public synchronized void updateAllInfo() {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("UPDATE servers SET gamestate=?, mapname=?, players=?, lastchange=? WHERE nickname=?");
			ps.setInt(1, Servers.getState().getNumberFromState(Core.get().getGame().getState()));
			ps.setString(2, Core.get().getGame().getState() == GameState.LOBBY ? Core.get().getMapManager().getLobby().getName() : Core.get().getGame().getCurrentMap().getName());
			ps.setInt(3, Core.get().getGame().getPlayers().size());
			ps.setString(4, "" + System.currentTimeMillis() / 1000);
			ps.setString(5, getName());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

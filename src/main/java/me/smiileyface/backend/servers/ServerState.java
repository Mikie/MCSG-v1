package me.smiileyface.backend.servers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.smiileyface.backend.Servers;
import me.smiileyface.backend.database.DBConnection;
import me.smiileyface.mcsg.game.GameState;

public class ServerState {

	public GameState getStateFromNumber(int state) {
		switch(state) {
			case 0:
				return GameState.LOBBY;
			case 1:
				return GameState.STARTING;
			case 2:
				return GameState.IN_GAME;
			case 3:
				return GameState.DEATHMATCH;
			case 4:
				return GameState.ENDING;
		}
		return GameState.DEAD;
	}
	
	public int getNumberFromState(GameState state) {
		switch(state) {
			case LOBBY:
				return 0;
			case STARTING:
				return 1;
			case IN_GAME:
				return 2;
			case DEATHMATCH:
				return 3;
			case ENDING:
				return 4;
			default:
				return 10;
		}
	}
	
	public synchronized int getGameState(String name) {
		name = name.toLowerCase();
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT gamestate FROM servers WHERE nickname=?");
			ps.setString(1, name);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("gamestate");
			} else {
				rs.close();
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 10;
	}
	
	public synchronized void setGameState(int state) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("UPDATE servers SET gamestate=?, lastchange=? WHERE nickname=?");
			ps.setInt(1, state);
			ps.setString(2, "" + System.currentTimeMillis() / 1000);
			ps.setString(3, Servers.getData().getName());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

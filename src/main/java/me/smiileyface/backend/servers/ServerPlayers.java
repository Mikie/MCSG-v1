package me.smiileyface.backend.servers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.smiileyface.backend.Servers;
import me.smiileyface.backend.database.DBConnection;

public class ServerPlayers {

	public int getMaxPlayers(String name) {
		name = name.toLowerCase();
		if(name.startsWith("sg")) {
			return 24;
		}
		if(name.startsWith("bigsg")) {
			return 48;
		}
		return 24;
	}
	
	public synchronized int getPlayers(String name) {
		name = name.toLowerCase();
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT players FROM servers WHERE nickname=?");
			ps.setString(1, name);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt("players");
			} else {
				rs.close();
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public synchronized void setPlayers(int players) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c
					.prepareStatement("UPDATE servers SET players=? WHERE nickname=?");
			ps.setInt(1, players);
			ps.setString(2, Servers.getData().getName());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.getLogger(ServerPlayers.class.getName()).log(Level.SEVERE,
					null, e);
		}
	}
}

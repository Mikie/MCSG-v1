package me.smiileyface.backend.stats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.smiileyface.backend.database.DBConnection;

public class PlayerGamesPlayed {

	public synchronized int getGamesPlayed(Player player) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT gamesplayed FROM stats WHERE uuid=?");
			ps.setString(1, player.getUniqueId().toString());
			
			ResultSet rs = ps.executeQuery();
			boolean has = rs.next();
			
			if(has) {
				return rs.getInt("gamesplayed");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public synchronized void setGamesPlayed(UUID uuid, int gamesplayed) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c
					.prepareStatement("UPDATE stats SET gamesplayed=? WHERE uuid=?");
			ps.setInt(1, gamesplayed);
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

package me.smiileyface.backend.stats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.smiileyface.backend.database.DBConnection;

public class PlayerPoints {

	public synchronized int getPoints(Player player) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT points FROM stats WHERE uuid=?");
			ps.setString(1, player.getUniqueId().toString());
			
			ResultSet rs = ps.executeQuery();
			boolean has = rs.next();
			
			if(has) {
				return rs.getInt("points");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public synchronized void setPoints(UUID uuid, int points) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c
					.prepareStatement("UPDATE stats SET points=? WHERE uuid=?");
			ps.setInt(1, points);
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

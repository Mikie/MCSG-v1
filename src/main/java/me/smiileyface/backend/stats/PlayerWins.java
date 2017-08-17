package me.smiileyface.backend.stats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.smiileyface.backend.database.DBConnection;

public class PlayerWins {

	public synchronized int getWins(Player player) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT wins FROM stats WHERE uuid=?");
			ps.setString(1, player.getUniqueId().toString());
			
			ResultSet rs = ps.executeQuery();
			boolean has = rs.next();
			
			if(has) {
				return rs.getInt("wins");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public synchronized void setWins(UUID uuid, int wins) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c
					.prepareStatement("UPDATE stats SET wins=? WHERE uuid=?");
			ps.setInt(1, wins);
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

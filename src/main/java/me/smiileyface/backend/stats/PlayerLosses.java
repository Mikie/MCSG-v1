package me.smiileyface.backend.stats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.smiileyface.backend.database.DBConnection;

public class PlayerLosses {

	public synchronized int getLosses(Player player) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT losses FROM stats WHERE uuid=?");
			ps.setString(1, player.getUniqueId().toString());
			
			ResultSet rs = ps.executeQuery();
			boolean has = rs.next();
			
			if(has) {
				return rs.getInt("losses");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public synchronized void setLosses(UUID uuid, int losses) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c
					.prepareStatement("UPDATE stats SET losses=? WHERE uuid=?");
			ps.setInt(1, losses);
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

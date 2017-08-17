package me.smiileyface.backend.servers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.smiileyface.backend.database.DBConnection;

public class ServerMap {

	public synchronized String getMapName(String name) {
		name = name.toLowerCase();
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT mapname FROM servers WHERE nickname=?");
			ps.setString(1, name);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				return rs.getString("mapname");
			} else {
				rs.close();
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Unknown Map";
	}
	
	public synchronized void setMapName(String name) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("UPDATE servers SET mapname=? WHERE nickname=?");
			ps.setString(1, name);
			ps.setString(2, "");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

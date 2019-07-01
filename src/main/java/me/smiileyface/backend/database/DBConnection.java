package me.smiileyface.backend.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.smiileyface.mcsg.Core;

public class DBConnection {

	private static DBConnection dbc = new DBConnection();
	private Connection c = null;
	
	public static DBConnection getDatabase() {
		return dbc;
	}
	
	public Connection getConnection() {
		try {
			if (c == null || c.isClosed()) {
				c = DriverManager.getConnection(
						"jdbc:mysql://" + Core.get().getConfig().getString("SQL.host")+ "/" + Core.get().getConfig().getString("SQL.database"), Core.get().getConfig().getString("SQL.username"),
						Core.get().getConfig().getString("SQL.password"));
			}
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

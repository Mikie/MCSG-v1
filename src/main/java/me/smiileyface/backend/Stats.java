package me.smiileyface.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import me.smiileyface.backend.database.DBConnection;
import me.smiileyface.backend.stats.PlayerDeaths;
import me.smiileyface.backend.stats.PlayerGamesPlayed;
import me.smiileyface.backend.stats.PlayerKills;
import me.smiileyface.backend.stats.PlayerLifespan;
import me.smiileyface.backend.stats.PlayerLosses;
import me.smiileyface.backend.stats.PlayerPoints;
import me.smiileyface.backend.stats.PlayerWins;

public class Stats {

	private static PlayerDeaths pd = new PlayerDeaths();
	private static PlayerGamesPlayed pgp = new PlayerGamesPlayed();
	private static PlayerKills pk = new PlayerKills();
	private static PlayerLifespan plife = new PlayerLifespan();
	private static PlayerLosses pl = new PlayerLosses();
	private static PlayerPoints pp = new PlayerPoints();
	private static PlayerWins pw = new PlayerWins();

	public static PlayerDeaths getDeaths() {
		return pd;
	}

	public static PlayerGamesPlayed getGamesPlayed() {
		return pgp;
	}

	public static PlayerKills getKills() {
		return pk;
	}

	public static PlayerLifespan getLifespan() {
		return plife;
	}

	public static PlayerLosses getLosses() {
		return pl;
	}

	public static PlayerPoints getPoints() {
		return pp;
	}

	public static PlayerWins getWins() {
		return pw;
	}

	public static synchronized void insertIntoStats(Player player) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c
					.prepareStatement("INSERT INTO stats (username, uuid) VALUES (?,?)");
			ps.setString(1, player.getName());
			ps.setString(2, player.getUniqueId().toString());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized boolean isInDatabase(Player player) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT username FROM stats WHERE uuid=?");
			ps.setString(1, player.getUniqueId().toString());

			ResultSet rs = ps.executeQuery();
			boolean has = rs.next();

			if (has) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}

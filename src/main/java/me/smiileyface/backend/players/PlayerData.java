package me.smiileyface.backend.players;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import me.smiileyface.backend.Players;
import me.smiileyface.backend.Stats;
import me.smiileyface.backend.database.DBConnection;
import me.smiileyface.mcsg.player.GamePlayer;

public class PlayerData {

	public synchronized void addPlayerToDatabase(Player player) {
		if (isInDatabase(player)) {
			return;
		}
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c
					.prepareStatement("INSERT INTO players(username, uuid, lastip, rank) VALUES (?,?,?,?)");
			ps.setString(1, player.getName());
			ps.setString(2, player.getUniqueId().toString());
			ps.setString(3, player.getAddress().getHostString());
			ps.setInt(4, 0);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized boolean isInDatabase(Player player) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT username FROM players WHERE uuid=?");
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

	public synchronized GamePlayer getPlayerFromName(Player p) {
		GamePlayer player = null;
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT * FROM players WHERE username=?");
			ps.setString(1, p.getName());

			ResultSet rs = ps.executeQuery();
			boolean has = rs.next();

			if (has) {
				String uuid = rs.getString("uuid");
				if (uuid == null || uuid.length() == 0) {
					Players.getData().addPlayerToDatabase(p);
					Stats.insertIntoStats(p);
				}
				player = new GamePlayer(p, 
						rs.getInt("rank"),
						Long.parseLong(rs.getString("rankexpiry")));
				return player;
			} else {
				throw new NullPointerException();
			}
		} catch (SQLException e) {
			Logger.getLogger(PlayerData.class.getName()).log(Level.SEVERE,
					null, e);
		} catch (NullPointerException e) {
			this.addPlayerToDatabase(p);
			Stats.insertIntoStats(p);
		}
		return player;
	}

	public synchronized GamePlayer getPlayerFromUUID(Player p) {
		GamePlayer player = null;
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT * FROM players WHERE uuid=?");
			ps.setString(1, p.getUniqueId().toString());

			ResultSet rs = ps.executeQuery();
			boolean has = rs.next();

			if (has) {
				player = new GamePlayer(p, 
						rs.getInt("rank"),
						Long.parseLong(rs.getString("rankexpiry")));
				return player;
			} else {
				addPlayerToDatabase(p);
				Stats.insertIntoStats(p);
				player = new GamePlayer(p);
				return player;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return player;
	}
	
	public synchronized String getNameFromUUID(String uuid) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c
					.prepareStatement("SELECT * FROM players WHERE uuid=?");
			ps.setString(1, uuid);

			ResultSet rs = ps.executeQuery();
			boolean has = rs.next();

			if (has) {
				return rs.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "???";
	}
	
	public synchronized void savePlayerToDB(Player p) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("UPDATE players SET username=? lastip=? WHERE uuid=?");
			ps.setString(1, p.getName());
			ps.setString(2, p.getAddress().getHostString());
			ps.setString(3, p.getUniqueId().toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void updateNameFromUUID(Player p) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c
					.prepareStatement("UPDATE players SET username=? WHERE uuid=?");
			ps.setString(1, p.getName());
			ps.setString(2, p.getUniqueId().toString());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

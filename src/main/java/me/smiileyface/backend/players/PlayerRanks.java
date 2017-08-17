package me.smiileyface.backend.players;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import me.smiileyface.backend.Players;
import me.smiileyface.backend.database.DBConnection;

public class PlayerRanks {

	public Rank getRankFromID(int id) {
		switch(id) {
			case 1:
				return Rank.IRON;
			case 2:
				return Rank.GOLD;
			case 3:
				return Rank.DIAMOND;
			case 4:
				return Rank.VIP;
			case 5:
				return Rank.MOD;
			case 6:
				return Rank.SR_MOD;
			case 7:
				return Rank.ADMIN;
			case 8:
				return Rank.DEV;
			case 9:
				return Rank.OWNER;
		}
		return Rank.REGULAR;
	}
	
	public synchronized Rank getRankFromDatabase(Player player) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT rank FROM players WHERE uuid=?");
			ps.setString(1, player.getUniqueId().toString());
			
			ResultSet rs = ps.executeQuery();
			boolean has = rs.next();
			
			if(!has) {
				rs.close();
				ps.close();
				return Rank.REGULAR;
			} else {
				return getRankFromID(rs.getInt("rank"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Rank.REGULAR;
	}
	
	public void setRank(Player player, int rank) {
		setRank(player, rank, 0);
	}
	
	public synchronized void setRank(Player player, int rank, long endTime) {
		if(!Players.getData().isInDatabase(player)) {
			Players.getData().addPlayerToDatabase(player);
		}
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("UPDATE players SET rank=?, rankexpiry=? WHERE uuid=?");
			ps.setInt(1, rank);
			ps.setString(2, endTime + "");
			ps.setString(3, player.getUniqueId().toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

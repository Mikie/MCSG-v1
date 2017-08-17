package me.smiileyface.backend.players;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.smiileyface.backend.database.DBConnection;
import me.smiileyface.backend.punishments.Punishment;
import me.smiileyface.backend.punishments.PunishmentType;

public class PlayerPunishment {

	public int getNewId() {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT * FROM punishments;");
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			return rs.getInt(1) + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Punishment getPunishment(int id) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT * FROM punishments WHERE id=" + id + ";");
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				Punishment punishment = new Punishment(UUID.fromString(rs.getString("punished")),
						rs.getString("punishedIPAddress"), 
						UUID.fromString(rs.getString("issuer")),
						PunishmentType.fromString(rs.getString("type")),
						rs.getString("reason"), rs.getString("time"),
						rs.getLong("end"));
				
				punishment.setId(rs.getInt(id));
				return punishment;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Punishment> getPunishmentsList(UUID player) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT * FROM punishments WHERE punished='" + player.toString() + "';");
			ResultSet rs = ps.executeQuery();
			
			List<Punishment> punishments = new ArrayList<Punishment>();
			while (rs.next()) {
				Punishment punishment = new Punishment(UUID.fromString(rs.getString("punished")),
						rs.getString("punishedIPAddress"), 
						UUID.fromString(rs.getString("issuer")),
						PunishmentType.fromString(rs.getString("type")),
						rs.getString("reason"), 
						rs.getString("time"),
						rs.getLong("end"));

				punishment.setId(rs.getInt("id"));
				punishments.add(punishment);
			}

			return punishments;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Punishment[] getPunishments(UUID player) {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT * FROM punishments WHERE punished='" + player.toString() + "';");
			ResultSet rs = ps.executeQuery();
			
			List<Punishment> punishments = new ArrayList<Punishment>();
			while (rs.next()) {
				Punishment punishment = new Punishment(UUID.fromString(rs.getString("punished")),
						rs.getString("punishedIPAddress"), 
						UUID.fromString(rs.getString("issuer")),
						PunishmentType.fromString(rs.getString("type")),
						rs.getString("reason"), 
						rs.getString("time"),
						rs.getLong("end"));

				punishment.setId(rs.getInt("id"));
				punishments.add(punishment);
			}

			return (Punishment[])punishments.toArray(new Punishment[punishments.size()]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Punishment[0];
	}
	
	public boolean hasActiveBan(UUID player) {
		for(Punishment punishment : getPunishments(player)) {
			if(((punishment.getType() == PunishmentType.PERMANENT_BAN) || (punishment.getType() == PunishmentType.TEMPORARY_BAN)) && (punishment.isActive())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasActiveMute(UUID player) {
		for(Punishment punishment : getPunishments(player)) {
			if(((punishment.getType() == PunishmentType.PERMANENT_MUTE) || (punishment.getType() == PunishmentType.TEMPORARY_MUTE)) && (punishment.isActive())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasActiveIPBan(UUID player) {
		for(Punishment punishment : getPunishments(player)) {
			if((punishment.getType() == PunishmentType.IP_BAN) && (punishment.isActive())) {
				return true;
			}
		}
		return false;
	}
	
	public Punishment getActiveBan(UUID player) {
		for (Punishment punishment : getPunishments(player)) {
			if (((punishment.getType() == PunishmentType.PERMANENT_BAN) || (punishment.getType() == PunishmentType.TEMPORARY_BAN)) && (punishment.isActive())) {
				return punishment;
			}
		}
		return null;
	}
	
	public Punishment getActiveMute(UUID player) {
		for (Punishment punishment : getPunishments(player)) {
			if (((punishment.getType() == PunishmentType.PERMANENT_MUTE) || (punishment.getType() == PunishmentType.TEMPORARY_MUTE)) && (punishment.isActive())) {
				return punishment;
			}
		}
		return null;
	}
	
	public Punishment getActiveIPBan(UUID player) {
		for (Punishment punishment : getPunishments(player)) {
			if ((punishment.getType() == PunishmentType.IP_BAN) && (punishment.isActive())) {
				return punishment;
			}
		}
		return null;
	}
	
	public Punishment getLatestPunishment(UUID player) {
		Punishment[] punishments = getPunishments(player);
		return punishments[(punishments.length - 1)];
	}
	
	public Punishment[] getBans(UUID player) {
		List<Punishment> punishments = new ArrayList<Punishment>();
		for (Punishment punishment : getPunishments(player)) {
			if (((punishment.getType() == PunishmentType.PERMANENT_BAN) || (punishment.getType() == PunishmentType.TEMPORARY_BAN))) {
				punishments.add(punishment);
			}
		}
		return (Punishment[]) punishments.toArray(new Punishment[punishments.size()]);
	}
	
	public Punishment[] getMutes(UUID player) {
		List<Punishment> punishments = new ArrayList<Punishment>();
		for (Punishment punishment : getPunishments(player)) {
			if (((punishment.getType() == PunishmentType.PERMANENT_MUTE) || (punishment.getType() == PunishmentType.TEMPORARY_MUTE))) {
				punishments.add(punishment);
			}
		}
		return (Punishment[]) punishments.toArray(new Punishment[punishments.size()]);
	}
}

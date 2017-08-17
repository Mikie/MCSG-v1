package me.smiileyface.backend.punishments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import me.smiileyface.backend.Players;
import me.smiileyface.backend.database.DBConnection;

public class Punishment {

	private UUID punished;
	private UUID issuer;
	private PunishmentType type;
	private String reason;
	private long end;
	private int id = Players.getPunishment().getNewId();
	private String time;
	private String ipAddress;
	private boolean active = true;
	
	public Punishment(UUID punished, String ipAddress, UUID issuer, PunishmentType type, String reason, String time, long end) {
		this.punished = punished;
		this.ipAddress = ipAddress;
		this.issuer = issuer;
		this.type = type;
		this.reason = reason;
		this.time = time;
		this.end = end;
	}
	
	public UUID getPunished() {
		return punished;
	}
	
	public String getPunishedIP() {
		return ipAddress;
	}
	
	public UUID getIssuer() {
		return issuer;
	}
	
	public PunishmentType getType() {
		return type;
	}
	
	public String getReason() {
		return reason;
	}
	
	public long getEnd() {
		return end;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTime() {
		return time;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean flag) {
		this.active = flag;
	}
	
	public void save() {
		try {
			Connection c = DBConnection.getDatabase().getConnection();
			PreparedStatement ps = c.prepareStatement("SELECT * FROM punishments WHERE id=" + getId() + ";");
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			if(rs.getInt(1) == 0) {
				c.createStatement().execute("INSERT INTO punishments (id, punished, punishedIPAddress, issuer, type, reason, time, end) VALUES("
						+ getId()
						+ ", '"
						+ getPunished().toString()
						+ "', '"
						+ ipAddress
						+ "', '"
						+ getIssuer().toString()
						+ "', '"
						+ getType().getName()
						+ "', '"
						+ getReason()
						+ "', '"
						+ getTime()
						+ "', "
						+ getEnd()
						+ ");");
			} else {
				c.createStatement().execute("UPDATE punishments SET "
						+ "punished='" + getPunished().toString()
						+"', punishedIpAddress='" + ipAddress
						+ "', issuer='"+ getIssuer().toString() 
						+ "', type='" + getType().getName()
						+ ", reason='" + getReason() 
						+ "', time='" + getTime()
						+ "', end=" + getEnd()
						+ " WHERE id=" + getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

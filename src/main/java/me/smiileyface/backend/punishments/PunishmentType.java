package me.smiileyface.backend.punishments;

public enum PunishmentType {

	IP_BAN("IP Ban"),
	TEMPORARY_BAN("Temporary Ban"),
	PERMANENT_BAN("Permanent Ban"),
	TEMPORARY_MUTE("Temporary Mute"),
	PERMANENT_MUTE("Permanent Mute"),
	KICK("Kick"),
	WARN("Warning");
	
	String name;
	
	private PunishmentType(String type) {
		this.name = type;
	}
	
	public String getName() {
		return name;
	}
	
	public static PunishmentType fromString(String name) {
		for(PunishmentType type : values()) {
			if(type.getName().equals(name)) {
				return type;
			}
		}
		return null;
	}
}

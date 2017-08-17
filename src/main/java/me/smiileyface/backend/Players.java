package me.smiileyface.backend;

import me.smiileyface.backend.players.PlayerData;
import me.smiileyface.backend.players.PlayerPunishment;
import me.smiileyface.backend.players.PlayerRanks;

public class Players {

	private static PlayerData pd = new PlayerData();
	private static PlayerPunishment pp = new PlayerPunishment();
	private static PlayerRanks pr = new PlayerRanks();
	
	public static PlayerData getData() {
		return pd;
	}
	
	public static PlayerPunishment getPunishment() {
		return pp;
	}
	
	public static PlayerRanks getRanks() {
		return pr;
	}
}

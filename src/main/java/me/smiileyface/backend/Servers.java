package me.smiileyface.backend;

import me.smiileyface.backend.servers.ServerData;
import me.smiileyface.backend.servers.ServerMap;
import me.smiileyface.backend.servers.ServerPlayers;
import me.smiileyface.backend.servers.ServerState;

public class Servers {

	private static ServerData sd = new ServerData();
	private static ServerMap sm = new ServerMap();
	private static ServerPlayers sp = new ServerPlayers();
	private static ServerState ss = new ServerState();
	
	public static ServerData getData() {
		return sd;
	}
	
	public static ServerMap getMap() {
		return sm;
	}
	
	public static ServerPlayers getPlayers() {
		return sp;
	}
	
	public static ServerState getState() {
		return ss;
	}
}

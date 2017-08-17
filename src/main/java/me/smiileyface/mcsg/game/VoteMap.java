package me.smiileyface.mcsg.game;

import me.smiileyface.mcsg.Core;

public class VoteMap {

	public String mapName;
	public int mapID;
	public int votes;
	
	public VoteMap(int mapID) {
		this.mapID = mapID;
		this.votes = 0;
		mapName = Core.getMaps().getString(mapID + ".name");
	}
	
	public void addVote(int amount) {
		this.votes += amount;
	}
}

package me.smiileyface.mcsg.game;

public enum GameState {

	LOBBY("Lobby"),
	STARTING("PreGame"),
	IN_GAME("LiveGame"),
	DEATHMATCH("Deathmatch"),
	ENDING("Ending"),
	DEAD("Dead");
	
	private String name;
	
	GameState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}

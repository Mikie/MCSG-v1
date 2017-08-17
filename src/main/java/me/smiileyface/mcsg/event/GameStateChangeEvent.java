package me.smiileyface.mcsg.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.smiileyface.mcsg.game.GameState;

public class GameStateChangeEvent extends Event {

	public static HandlerList handlers = new HandlerList();
	
	public GameState state;
	
	public GameStateChangeEvent(GameState state) {
		this.state = state;
	}
	
	public GameState getState() {
		return state;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}

package me.smiileyface.mcsg.map;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import me.smiileyface.mcsg.Core;
import me.smiileyface.utils.StringUtils;

public class Lobby {

	private String name;
	private String worldName;
	
	public Lobby(String name, String worldName) {
		this.name = name;
		this.worldName = worldName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getWorldName() {
		return worldName;
	}
	
	public World getWorld() {
		if (Bukkit.getWorld(worldName) == null) {
			Bukkit.createWorld(new WorldCreator(worldName));
		}
		return Bukkit.getWorld(worldName);
	}
	
	public Location getSpawn() {
		return StringUtils.strToLoc(getWorld(), Core.get().getConfig().getString("lobby.spawn-location"));
	}
	
	public void setSpawn(Location loc) {
		Core.get().getConfig().set("lobby.spawn-location", StringUtils.locToStr(loc));
		try {
			Core.maps.save(new File(Core.get().getDataFolder(), "maps.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

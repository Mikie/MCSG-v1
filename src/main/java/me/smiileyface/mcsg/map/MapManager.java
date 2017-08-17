package me.smiileyface.mcsg.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.smiileyface.mcsg.Core;
import me.smiileyface.utils.StringUtils;
import me.smiileyface.utils.ZipUtils;

public class MapManager {

	private List<Map> maps = new ArrayList<Map>();

	private Lobby lobby = null;

	public void setupMap(Map map) {
		maps.add(map);
	}

	public void setupLobby(Lobby lobby) {
		this.lobby = lobby;
	}

	public Lobby getLobby() {
		return lobby;
	}

	public List<Map> getMaps() {
		return maps;
	}

	public void resetMaps() {
		maps.clear();
	}

	public void randomizeMaps(int amount) {
		if (maps.size() <= 0)
			return;
		List<Map> maps1 = new ArrayList<Map>(maps);
		Collections.shuffle(maps1);
		resetMaps();
		for (int i = 0; i < amount; i++) {
			if (maps1.size() > i) {
				maps.add(maps1.get(i));
			}
		}
	}

	public Map getMap(String name) {
		for (Map m : getMaps()) {
			if (m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		return null;
	}

	public Map getMap(Player player) {
		for (Map m : getMaps()) {
			if (m.getWorld().getPlayers().contains(player)) {
				return m;
			}
		}
		return null;
	}

	public void loadMap(Map map) {
		try {
			ZipUtils.extractZIP(new File(Core.get().getDataFolder() + "/maps/" + map.getName() + ".zip"), new File(map.getName()));
			map.world = Bukkit.createWorld(new WorldCreator(map.getName()));
			for (Entity e : map.world.getEntities()) {
				if (e instanceof Item) {
					e.remove();
					continue;
				}
				if (!(e instanceof LivingEntity))
					continue;
				if ((LivingEntity) e instanceof Creature) {
					e.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Location> getSpawns(Map map) {
		List<Location> locs = new ArrayList<Location>();
		for(int i = 0; i < Core.maps.getStringList("maps." + map.getName() + ".spawns").size(); i++) {
			locs.add(StringUtils.strToLoc(map.getWorld(), Core.maps.getStringList("maps." + map.getName() + ".spawns").get(i)));
		}
		return locs;
	}
	
	public List<Location> getTier2Chests(Map map) {
		List<Location> locs = new ArrayList<Location>();
		for(int i = 0; i < Core.maps.getStringList("maps." + map.getName() + ".tier2").size(); i++) {
			locs.add(StringUtils.strToLoc(map.getWorld(), Core.maps.getStringList("maps." + map.getName() + ".tier2").get(i)));
		}
		return locs;
	}
}

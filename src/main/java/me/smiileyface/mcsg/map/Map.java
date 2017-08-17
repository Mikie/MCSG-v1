package me.smiileyface.mcsg.map;

import org.bukkit.Location;
import org.bukkit.World;

public class Map implements Cloneable {

	public World world;
	private Location min, max;
	private String name;
	private String author;
	private String link;
	
	private int votes = 0;
	
	public Map(Location min, Location max, String name, String author, String link, Location middle, int radius) {
		this.min = min;
		this.max = max;
		this.name = name;
		this.author = author;
		this.link = link;
		
		if(radius > 0) {
			this.middle = middle;
			this.middle.setY(100);
			this.radius = radius;
		}
	}
	
	public boolean constainsBlock(Location v) {
		if(v.getWorld() != min.getWorld()) return false;
		final double x = v.getX();
		final double y = v.getY();
		final double z = v.getZ();
		return x >= min.getBlockX() && x < max.getBlockX() + 1 && y >= min.getBlockY() && y < max.getBlockY() + 1 && z >= min.getBlockZ() && z < max.getBlockZ() + 1;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setVotes(int votes) {
		this.votes = votes;
	}
	
	public int getVotes() {
		return votes;
	}
	
	public Location getMinimumLocation() {
		return min;
	}
	
	public Location getMaximumLocation() {
		return max;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getLink() {
		return link;
	}
	
	private Location middle;
	private int radius;
	
	public Location getMiddle() {
		return middle;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public double domeDistance(Location loc) {
		Location bloc = loc.clone();
		bloc.setY(0);
		return middle.distance(bloc);
	}
	
	public boolean isDomeEnabled() {
		return middle != null;
	}
}

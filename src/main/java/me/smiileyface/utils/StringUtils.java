package me.smiileyface.utils;

import java.text.DecimalFormat;

import org.bukkit.Location;
import org.bukkit.World;

public class StringUtils {

	public static String locToStr(Location loc) {
		DecimalFormat five = new DecimalFormat("#####.#####");
		DecimalFormat three = new DecimalFormat("#####.###");
		String x = String.valueOf(five.format(loc.getX()));
		String y = String.valueOf(three.format(loc.getY()));
		String z = String.valueOf(five.format(loc.getZ()));
		String yaw = String.valueOf(five.format(loc.getYaw()));
		String pitch = String.valueOf(five.format(loc.getPitch()));
		return x + "," + y + "," + z + "," + yaw + "," + pitch;
	}
	
	public static Location strToLoc(World world, String string) {
		String[] split = string.split(",");
		Double x = Double.parseDouble(split[0].trim());
		Double y = Double.parseDouble(split[1].trim());
		Double z = Double.parseDouble(split[2].trim());
		Float yaw = Float.parseFloat(split[3].trim());
		Float pitch = Float.parseFloat(split[3].trim());
		Location loc = new Location(world, x, y, z, yaw, pitch);
		return loc;
	}
}

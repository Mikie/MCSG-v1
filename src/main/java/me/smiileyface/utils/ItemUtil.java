package me.smiileyface.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.smiileyface.mcsg.SettingsManager;

public class ItemUtil {

	public static ItemStack unserializeItemStack(String path, SettingsManager file) {
		String i = file.getString(path);

		ItemStack item = new ItemStack(Material.AIR);
		ItemMeta meta = item.getItemMeta();
		if (i.contains(",")) {
			String[] data = i.replaceAll(" ", "").split(",");
			try {
				item.setType(Material.getMaterial(data[0]));
				item.setAmount(Integer.parseInt(data[1]));
			} catch (NullPointerException x) {
				item.setType(Material.getMaterial(Integer.parseInt(data[0])));
				item.setAmount(Integer.parseInt(data[1]));
			}
		} else {
			try {
				item.setType(Material.getMaterial(i));
			} catch (NullPointerException x) {
				item.setType(Material.getMaterial(Integer.parseInt(i)));
			}
		}
		return item;
	}

	public static ItemStack unserializeItemStack(String items) {
		ItemStack item = new ItemStack(Material.AIR);
		ItemMeta meta = item.getItemMeta();
		if (items.contains(",")) {
			String[] data = items.replaceAll(" ", "").split(",");
			if (data[0].contains(":")) {
				try {
					item.setType(Material.getMaterial(data[0].split(":")[0]));
					item.setDurability(Short.parseShort(data[0].split(":")[1]));
					item.setAmount(Integer.parseInt(data[1]));
				} catch (NullPointerException x) {
					try {
						item.setType(Material.getMaterial(Integer.parseInt(data[0].split(":")[0])));
						item.setDurability(Short.parseShort(data[0].split(":")[1]));
						item.setAmount(Integer.parseInt(data[1]));
					} catch (NullPointerException x1) {
						item.setType(Material.AIR);
						item.setDurability((short) 0);
						item.setAmount(1);
					}
				}
			} else {
				try {
					item.setType(Material.getMaterial(data[0]));
					item.setAmount(Integer.parseInt(data[1]));
				} catch (NullPointerException x) {
					try {
						item.setType(Material.getMaterial(Integer.parseInt(data[0])));
						item.setAmount(Integer.parseInt(data[1]));
					} catch (NullPointerException x1) {
						item.setType(Material.AIR);
						item.setAmount(1);
					}
				}
			}
		} else if (items.contains(":")) {
			String[] data = items.split(":");
			try {
				item.setType(Material.getMaterial(data[0]));
				item.setDurability(Short.parseShort(data[1]));
			} catch (NullPointerException x) {
				try {
					item.setType(Material.getMaterial(Integer.parseInt(data[0])));
					item.setDurability(Short.parseShort(data[0]));
				} catch (NullPointerException x1) {
					item.setType(Material.AIR);
					item.setDurability((short) 0);
				}
			}
		} else {
			try {
				item.setType(Material.getMaterial(items));
			} catch (NullPointerException x) {
				try {
					item.setType(Material.getMaterial(Integer.parseInt(items)));
				} catch (NullPointerException x1) {
					item.setType(Material.AIR);
				}
			}
		}
		return item;
	}
}

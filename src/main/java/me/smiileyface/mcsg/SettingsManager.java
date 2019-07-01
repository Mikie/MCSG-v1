package me.smiileyface.mcsg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SettingsManager {

	private File file;
	private FileConfiguration config;

	public SettingsManager(String fname) {
		this.file = new File(Core.get().getDataFolder(), fname + ".yml");
		if (!this.file.exists()) {
			try {
				this.file.createNewFile();
			} catch (IOException e) {
			}
		}
		this.config = YamlConfiguration.loadConfiguration(this.file);
	}

	public void save() {
		try {
			this.config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reload() {
		YamlConfiguration.loadConfiguration(this.file);
		save();
	}

	public String getString(String path) {
		return this.config.getString(path);
	}

	public int getInt(String path) {
		return this.config.getInt(path);
	}

	public double getDouble(String path) {
		return this.config.getDouble(path);
	}

	public boolean getBoolean(String path) {
		return this.config.getBoolean(path);
	}

	public boolean contains(String path) {
		return this.config.contains(path);
	}

	public ConfigurationSection createSection(String path) {
		return this.config.createSection(path);
	}

	public void createPath(String path, Object value) {
		if (this.config.get(path) == null) {
			this.config.set(path, value);
			save();
		}
	}

	public ArrayList<String> getList(String path) {
		return (ArrayList) this.config.getList(path);
	}

	public void createStringList(String path, String... values) {
		if (this.config.get(path) == null) {
			List<String> list = new ArrayList(Arrays.asList(values));
			this.config.set(path, list);
			save();
		}
	}

	public void createNumberList(String path, Integer... values) {
		if (this.config.get(path) == null) {
			List<Integer> list = new ArrayList(Arrays.asList(values));
			this.config.set(path, list);
			save();
		}
	}

	public void setPath(String path, Object value) {
		this.config.set(path, value);
		save();
	}

	public ConfigurationSection getSection(String path) {
		return this.config.getConfigurationSection(path);
	}

	public void removeSection(String path) {
		getSection(path).set(path, null);
	}
}

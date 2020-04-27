package com.cubepalace.cubechat.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.cubepalace.cubechat.CubeChat;

public class ConfigFile {

	private static ConfigFile instance;
	
	public static ConfigFile get() {
		instance = (instance == null) ? new ConfigFile() : instance;
		return instance;
	}
	
	private File file;
	private FileConfiguration config;
	
	private ConfigFile() {
		file = new File(CubeChat.get().getDataFolder(), "config.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void reload() {
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
}

package com.cubepalace.cubechat.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.cubepalace.cubechat.ChatOptions;
import com.cubepalace.cubechat.CubeChat;

public class PlayerFile {

	private static PlayerFile instance;
	
	public static PlayerFile get() {
		instance = (instance == null) ? new PlayerFile() : instance;
		return instance;
	}
	
	private File file;
	private FileConfiguration config;
	
	private PlayerFile() {
		file = new File(CubeChat.get().getDataFolder(), "players.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
		loadDefaults();
	}
	
	private void loadDefaults() {
		if(!config.isSet("players"))
			config.createSection("players");
		config.options().copyDefaults(true);
		save();
	}
	
	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reload() {
		config = YamlConfiguration.loadConfiguration(file);
		save();
	}

	public void setPlayerOptions(UUID uuid, ChatOptions options) {
		String player = "players." + uuid + ".";
		config.set(player + "name", options.getName());
		config.set(player + "viewMuted", options.canViewMuted());
		config.set(player + "viewShadowMuted", options.canViewShadowMuted());
		config.set(player + "isShadowMuted", options.isShadowMuted());
		config.set(player + "filter", options.hasFilter());
		save();
	}
	
	public ChatOptions getPlayerOptions(UUID uuid) {
		String player = "players." + uuid + ".";
		ChatOptions options = new ChatOptions();
		options.setUniqueId(uuid);
		options.setName(config.getString(player + "name"));
		options.setViewMuted(config.getBoolean(player + "viewMuted"));
		options.setViewShadowMuted(config.getBoolean(player + "viewShadowMuted"));
		options.setShadowMuted(config.getBoolean(player + "isShadowMuted"));
		options.setFilter(config.getBoolean(player + "filter"));
		return options;
	}
	
	public Map<UUID, ChatOptions> loadToMap() {
		Map<UUID, ChatOptions> options = new HashMap<>();
		config.getConfigurationSection("players").getKeys(false).forEach(p -> {
			UUID uuid = UUID.fromString(p);
			options.put(uuid, this.getPlayerOptions(uuid));
		});
		return options;
	}
	
}

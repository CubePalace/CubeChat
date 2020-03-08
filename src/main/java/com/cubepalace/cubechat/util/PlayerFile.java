package com.cubepalace.cubechat.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.cubepalace.cubechat.CubeChat;

public class PlayerFile {

	private CubeChat instance;
	private File file;
	private FileConfiguration config;
	
	public PlayerFile(CubeChat instance, String fileName) {
		this.instance = instance;
		file = new File(instance.getDataFolder(), fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
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
	
	public void updateMuteChatList() {
		List<UUID> uuidList = instance.getMutedChatIgnore();
		List<String> stringList = new ArrayList<String>();
		for (UUID uuid : uuidList) {
			stringList.add(uuid.toString());
		}
		config.set("noreadmutedchat", stringList);
		save();
	}
	
	public List<UUID> loadToListMuteChat() {
		List<UUID> list = new ArrayList<UUID>();
		
		for (String uuidStr : config.getStringList("noreadmutedchat")) {
			UUID uuid = UUID.fromString(uuidStr);
			list.add(uuid);
		}
		return list;
	}
	
	public void updateIgnoreShadowmuted() {
		List<UUID> uuidList = instance.getIgnoreShadowmuted();
		List<String> stringList = new ArrayList<String>();
		for (UUID uuid : uuidList) {
			stringList.add(uuid.toString());
		}
		config.set("noreadshadowmuted", stringList);
		save();
	}
	
	public List<UUID> loadToListIgnoreShadowmuted() {
		List<UUID> list = new ArrayList<UUID>();
		
		for (String uuidStr : config.getStringList("noreadshadowmuted")) {
			UUID uuid = UUID.fromString(uuidStr);
			list.add(uuid);
		}
		return list;
	}
	
	public void updateShadowmuted() {
		List<UUID> uuidList = instance.getShadowmuted();
		List<String> stringList = new ArrayList<String>();
		for (UUID uuid : uuidList) {
			stringList.add(uuid.toString());
		}
		config.set("shadowmuted", stringList);
		save();
	}
	
	public List<UUID> loadToListShadowmuted() {
		List<UUID> list = new ArrayList<UUID>();
		
		for (String uuidStr : config.getStringList("shadowmuted")) {
			UUID uuid = UUID.fromString(uuidStr);
			list.add(uuid);
		}
		return list;
	}
	
	public void updateNoFilter() {
		List<UUID> uuidList = instance.getNoFilter();
		List<String> stringList = new ArrayList<String>();
		for (UUID uuid : uuidList) {
			stringList.add(uuid.toString());
		}
		config.set("nofilter", stringList);
		save();
	}
	
	public List<UUID> loadToListNoFilter() {
		List<UUID> list = new ArrayList<UUID>();
		
		for (String uuidStr : config.getStringList("nofilter")) {
			UUID uuid = UUID.fromString(uuidStr);
			list.add(uuid);
		}
		return list;
		
	}
}

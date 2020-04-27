package com.cubepalace.cubechat;

import java.util.UUID;

import org.bukkit.Bukkit;

public class ChatOptions {

	private static ChatOptions instance;
	
	public static ChatOptions get() {
		instance = (instance == null) ? new ChatOptions() : instance;
		return instance;
	}
	
	private UUID uuid;
	private String name;
	
	public UUID getUniqueId() {
		return uuid;
	}
	
	public void setUniqueId(UUID uuid) {
		this.uuid = uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	private boolean viewMuted;
	private boolean viewShadowMuted;
	private boolean isShadowMuted;
	private boolean filter;
	
	public boolean canViewMuted() {
		return viewMuted;
	}
	
	public void setViewMuted(boolean viewMuted) {
		this.viewMuted = viewMuted;
	}
	
	public boolean canViewShadowMuted() {
		return viewShadowMuted;
	}
	
	public void setViewShadowMuted(boolean viewShadowMuted) {
		this.viewShadowMuted = viewShadowMuted;
	}
	
	public boolean isShadowMuted() {
		return isShadowMuted;
	}
	
	public void setShadowMuted(boolean isShadowMuted) {
		this.isShadowMuted = isShadowMuted;
	}
	
	public boolean hasFilter() {
		return filter;
	}
	
	public void setFilter(boolean filter) {
		this.filter = filter;
	}
	
	public static ChatOptions defaultOptions(UUID uuid) {
		ChatOptions options = new ChatOptions();
		options.setUniqueId(uuid);
		options.setName(Bukkit.getServer().getPlayer(uuid).getName());
		options.setViewMuted(false);
		options.setViewShadowMuted(false);
		options.setShadowMuted(false);
		options.setFilter(true);
		return options;
	}
	
}

package com.cubepalace.cubechat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolManager;
import com.cubepalace.cubechat.commands.ClearChat;
import com.cubepalace.cubechat.commands.CubeChatCmd;
import com.cubepalace.cubechat.commands.MuteChat;
import com.cubepalace.cubechat.commands.ShadowMute;
import com.cubepalace.cubechat.commands.ToggleFilter;
import com.cubepalace.cubechat.listeners.AdvertisingListener;
import com.cubepalace.cubechat.listeners.FilterListener;
import com.cubepalace.cubechat.listeners.MiscChatListener;
import com.cubepalace.cubechat.listeners.MuteChatListener;
import com.cubepalace.cubechat.listeners.ShadowMuteListener;
import com.cubepalace.cubechat.util.ConfigFile;
import com.cubepalace.cubechat.util.PlayerFile;

public class CubeChat extends JavaPlugin {

	private static CubeChat instance;
	
	private boolean chatMuted;
	private Map<UUID, ChatOptions> options = new HashMap<>();
	private Map<UUID, Long> cooldownMap;
	private ProtocolManager pm;
	private final String noperm = ChatColor.RED + "No permission.";
	private boolean listsChanged = false;

	public CubeChat() {
		instance = this;
		chatMuted = false;
	}

	@Override
	public void onEnable() {
		getLogger().info("Loading player options from file...");
		setup();
		getLogger().info("Loading complete");
		getLogger().info("Scheduling automatic file saving...");
		saveTimer();
		getLogger().info("Automatic file saving enabled");
		register();
		getLogger().info("CubeChat has been enabled");
	}

	@Override
	public void onDisable() {
		if (listsChanged) {
			getLogger().info("Saving player options to file...");
			options.forEach((k, v) -> {
				PlayerFile.get().setPlayerOptions(k, v);
			});
			options.clear();
			getLogger().info("Save complete");
			listsChanged = false;
		}
		getLogger().info("CubeChat has been disabled");
	}

	private void register() {
		getServer().getPluginManager().registerEvents(new MuteChatListener(this), this);
		getServer().getPluginManager().registerEvents(new ShadowMuteListener(this), this);
		getServer().getPluginManager().registerEvents(new MiscChatListener(), this);
		getServer().getPluginManager().registerEvents(new FilterListener(), this);
		getServer().getPluginManager().registerEvents(new AdvertisingListener(this), this);
		getCommand("clearchat").setExecutor(new ClearChat(this));
		getCommand("mutechat").setExecutor(new MuteChat(this));
		getCommand("cubechat").setExecutor(new CubeChatCmd(this));
		getCommand("shadowmute").setExecutor(new ShadowMute(this));
		getCommand("togglefilter").setExecutor(new ToggleFilter(this));
	}

	private void setup() {
		cooldownMap = new HashMap<UUID, Long>();
		if (!getDataFolder().exists())
			getDataFolder().mkdirs();
		PlayerFile.get();
		saveResource("config.yml", false);
		ConfigFile.get();
		options.clear();
		options.putAll(PlayerFile.get().loadToMap());
	}

	private void saveTimer() {
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				if (listsChanged) {
					getLogger().info("Saving player options to file...");
					options.forEach((k, v) -> {
						PlayerFile.get().setPlayerOptions(k, v);
					});
					getLogger().info("Save complete");
					listsChanged = false;
				}
			}
		}, 6000L, 6000L);
	}
	
	public String getNoPerm() {
		return noperm;
	}

	public static CubeChat get() {
		return instance;
	}

	public void toggleMute() {
		chatMuted = !chatMuted;
	}

	public boolean getChatMuted() {
		return chatMuted;
	}

	public int getMaxCaps() {
		return ConfigFile.get().getConfig().getInt("maxCapitals");
	}

	public int getMaxFlood() {
		return ConfigFile.get().getConfig().getInt("maxFlood");
	}

	public long getSpamCooldown() {
		return ConfigFile.get().getConfig().getLong("spamCooldown");
	}

	public int getCensorDistance() {
		return ConfigFile.get().getConfig().getInt("censorDistance");
	}
	
	public ChatOptions getOptions(UUID uuid) {
		return options.containsKey(uuid) ? options.get(uuid) : newDefault(uuid);
	}
	
	private ChatOptions newDefault(UUID uuid) {
		ChatOptions playerOptions = ChatOptions.defaultOptions(uuid);
		options.put(uuid, playerOptions);
		listsChanged = true;
		return playerOptions;
	}
	
	public void setOptions(UUID uuid, ChatOptions options) {
		this.options.put(uuid, options);
		listsChanged = true;
	}
	
	public void updateOptionsMap() {
		options.clear();
		options.putAll(PlayerFile.get().loadToMap());
		listsChanged = true;
	}

	public Map<UUID, Long> getCooldowns() {
		return cooldownMap;
	}

	public void addCooldown(UUID uuid) {
		cooldownMap.put(uuid, System.currentTimeMillis());
	}

	public ProtocolManager getPM() {
		return pm;
	}
}

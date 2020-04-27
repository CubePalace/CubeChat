package com.cubepalace.cubechat.listeners;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.cubepalace.cubechat.ChatOptions;
import com.cubepalace.cubechat.CubeChat;

public class ShadowMuteListener implements Listener {

	private CubeChat instance;
	
	public ShadowMuteListener(CubeChat instance) {
		this.instance = instance;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		ChatOptions options = instance.getOptions(uuid);
		if (options.isShadowMuted()) {
			for (Player p : instance.getServer().getOnlinePlayers()) {
				if (p.getUniqueId() != e.getPlayer().getUniqueId())
					e.getRecipients().remove(p);
				if (p.hasPermission("cubechat.shadowmute.read")) {
					if (options.canViewMuted()) {
						p.sendMessage(ChatColor.DARK_GRAY + "[Shadowmute] " + e.getPlayer().getName() + " tried to say: " + e.getMessage());
					}
				}
			}
		}
	}
}

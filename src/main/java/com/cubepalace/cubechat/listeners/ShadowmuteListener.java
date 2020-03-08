package com.cubepalace.cubechat.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.cubepalace.cubechat.CubeChat;

public class ShadowmuteListener implements Listener {

	private CubeChat instance;
	
	public ShadowmuteListener(CubeChat instance) {
		this.instance = instance;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (instance.getShadowmuted().contains(e.getPlayer().getUniqueId())) {
			for (Player p : instance.getServer().getOnlinePlayers()) {
				if (p.getUniqueId() != e.getPlayer().getUniqueId())
					e.getRecipients().remove(p);
				if (p.hasPermission("cubechat.shadowmute.read")) {
					if (!instance.getIgnoreShadowmuted().contains(p.getUniqueId())) {
						p.sendMessage(ChatColor.DARK_GRAY + "[Shadowmute] " + e.getPlayer().getName() + " tried to say: " + e.getMessage());
					}
				}
			}
		}
	}
}

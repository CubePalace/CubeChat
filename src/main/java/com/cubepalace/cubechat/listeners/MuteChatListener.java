package com.cubepalace.cubechat.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.cubepalace.cubechat.CubeChat;

public class MuteChatListener implements Listener {

	private CubeChat instance;

	public MuteChatListener(CubeChat instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (instance.getChatMuted()) {
			if (e.getPlayer().hasPermission("cubechat.mutechat.exempt"))
				return;
			for (Player p : instance.getServer().getOnlinePlayers()) {
				if (p.hasPermission("cubechat.mutechat.read")) {
					if (!instance.getMutedChatIgnore().contains(p.getUniqueId())) {
						p.sendMessage(ChatColor.DARK_GRAY + "[MutedChat] " + e.getPlayer().getName() + " tried to say: "
								+ e.getMessage());
					}
				}
			}
			instance.getLogger()
					.info(ChatColor.DARK_GRAY + "[MutedChat] " + e.getPlayer().getName() + " tried to say: " + e.getMessage());
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.GOLD + "You cannot talk, the chat is muted");
		}
	}
}

package com.cubepalace.cubechat.listeners;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.cubepalace.cubechat.CubeChat;

public class MiscChatListener implements Listener {

	private CubeChat instance;

	public MiscChatListener(CubeChat instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onCapsChat(AsyncPlayerChatEvent e) {
		if (instance.getMaxCaps() == -1)
			return;
		Player p = e.getPlayer();
		if (p.hasPermission("cubechat.caps.exempt"))
			return;

		int capsCount = 0;
		for (char c : e.getMessage().toCharArray()) {
			if (Character.isUpperCase(c))
				capsCount++;
		}

		if (capsCount > instance.getMaxCaps()) {
			e.setMessage(e.getMessage().toLowerCase());
			p.sendMessage(ChatColor.GOLD + "You used too many capitals, your message was converted to lowercase");
		}
	}

	@EventHandler
	public void onFloodChat(AsyncPlayerChatEvent e) {
		if (instance.getMaxFlood() == 0)
			return;
		Player p = e.getPlayer();
		int maxFlood = instance.getMaxFlood();
		if (p.hasPermission("cubechat.flood.exempt"))
			return;
		String newMessage = e.getMessage().replaceAll("(?i)([a-z])\\1{" + maxFlood + ",}", "$1");
		if (!e.getMessage().equals(newMessage))
			p.sendMessage(ChatColor.GOLD + "You used too many of the same character, your message has been shortened");
		e.setMessage(newMessage);
	}

	@EventHandler
	public void onSpamChat(AsyncPlayerChatEvent e) {
		if (instance.getSpamCooldown() == 0)
			return;
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		if (p.hasPermission("cubechat.spam.exempt"))
			return;
		if (instance.getCooldowns().containsKey(uuid)) {
			long timeUntilChat = instance.getCooldowns().get(uuid) + instance.getSpamCooldown();
			long now = System.currentTimeMillis();
			if (now < timeUntilChat) {
				p.sendMessage(ChatColor.RED + "You're sending messages too fast! You can chat again in "
						+ (timeUntilChat - now) / 1000 + " second" + (((timeUntilChat - now) / 1000) == 1 ? "" : "s"));
				e.setCancelled(true);
			} else
				instance.addCooldown(uuid);
		} else
			instance.addCooldown(uuid);
	}
}

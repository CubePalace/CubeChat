package com.cubepalace.cubechat.listeners;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.cubepalace.cubechat.ChatOptions;
import com.cubepalace.cubechat.CubeChat;
import com.cubepalace.cubechat.util.ConfigFile;
import com.cubepalace.cubechat.util.Filter;

public class MiscChatListener implements Listener {

	@EventHandler
	public void onCapsChat(AsyncPlayerChatEvent e) {
		if (CubeChat.get().getMaxCaps() == -1)
			return;
		Player p = e.getPlayer();
		if (p.hasPermission("cubechat.caps.exempt"))
			return;

		int capsCount = 0;
		for (char c : e.getMessage().toCharArray()) {
			if (Character.isUpperCase(c))
				capsCount++;
		}

		if (capsCount > CubeChat.get().getMaxCaps()) {
			e.setMessage(e.getMessage().toLowerCase());
			p.sendMessage(ChatColor.GOLD + "You used too many capitals, your message was converted to lowercase");
		}
	}

	@EventHandler
	public void onFloodChat(AsyncPlayerChatEvent e) {
		if (CubeChat.get().getMaxFlood() == 0)
			return;
		Player p = e.getPlayer();
		int maxFlood = CubeChat.get().getMaxFlood();
		if (p.hasPermission("cubechat.flood.exempt"))
			return;
		String newMessage = e.getMessage().replaceAll("(?i)([a-z])\\1{" + maxFlood + ",}", "$1");
		if (!e.getMessage().equals(newMessage))
			p.sendMessage(ChatColor.GOLD + "You used too many of the same character, your message has been shortened");
		e.setMessage(newMessage);
	}

	@EventHandler
	public void onSpamChat(AsyncPlayerChatEvent e) {
		if (CubeChat.get().getSpamCooldown() == 0)
			return;
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		if (p.hasPermission("cubechat.spam.exempt"))
			return;
		if (CubeChat.get().getCooldowns().containsKey(uuid)) {
			long timeUntilChat = CubeChat.get().getCooldowns().get(uuid) + CubeChat.get().getSpamCooldown();
			long now = System.currentTimeMillis();
			if (now < timeUntilChat) {
				p.sendMessage(ChatColor.RED + "You're sending messages too fast! You can chat again in "
						+ (timeUntilChat - now) / 1000 + " second" + (((timeUntilChat - now) / 1000) == 1 ? "" : "s"));
				e.setCancelled(true);
			} else
				CubeChat.get().addCooldown(uuid);
		} else
			CubeChat.get().addCooldown(uuid);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onChatFilter(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		new BukkitRunnable() {
			public void run() {
				List<String> always = ConfigFile.get().getConfig().getStringList("hardCensor");
				List<String> toggle = ConfigFile.get().getConfig().getStringList("softCensor");
				// Format the message, before we do anything (since this is the lowest-priority)
				String message = 
						String.format(event.getFormat(), new Object[] { event.getPlayer().getDisplayName(), event.getMessage() });
				
				Bukkit.getServer().getConsoleSender().sendMessage(message);
				
				Map<Player, ChatOptions> recipients = event.getRecipients().stream()
						.collect(Collectors.toMap(k -> k, v -> CubeChat.get().getOptions(v.getUniqueId())));
				Map<Player, String> messages = event.getRecipients().stream()
						.collect(Collectors.toMap(k -> k, v -> message));
				
				System.out.println(messages.toString());
				
				toggle.stream().forEach(word -> {
					if(message.contains(word) || message.contains(Filter.convert(word))) {
						recipients.forEach((k, v) -> {
							String filtered = message;
							if(v.hasFilter()) {
								k.sendMessage("player has filter on, word: " + word);
								filtered = filter(message, word);
							}
							k.sendMessage("test: " + filtered);
							messages.replace(k, filtered);
						});
					}
				});
				
				System.out.println(messages.toString());
				
				always.stream().forEach(word -> {
					if(message.contains(word) || message.contains(Filter.convert(word))) {
						messages.forEach((k, v) -> {
							String filtered = filter(v, word);
							messages.put(k, filtered);
						});
					}
				});
				
				System.out.println(messages.toString());
				
				messages.forEach((k, v) -> {
					k.sendMessage(v);
				});
			}
		}.runTask(CubeChat.get());
	}
	
	private String filter(String message, String word) {
		// ((§[a-f|r|k-o|0-9]{1})+|\b)((f\W+)(u\W+)(c\W+)(k)\b)
		message = message.replaceAll(Pattern.compile("((§[a-f|r|k-o|0-9]{1})+|\\b)" + word + "\\b", Pattern.CASE_INSENSITIVE).pattern(), "****");
		message = message.replaceAll(Pattern.compile("((§[a-f|r|k-o|0-9]{1})+|\\b)" + Filter.convert(word) + "\\b", Pattern.CASE_INSENSITIVE).pattern(), "****");
		return message;
	}
	
}

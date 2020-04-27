package com.cubepalace.cubechat.listeners;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.cubepalace.cubechat.ChatOptions;
import com.cubepalace.cubechat.CubeChat;
import com.cubepalace.cubechat.util.ConfigFile;

public class FilterListener implements Listener {
	
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

				toggle.stream().forEach(word -> {
					if(construct(word).matcher(message).find()) {
						recipients.forEach((k, v) -> {
							String filtered = messages.get(k);
							if(v.hasFilter()) {
								filtered = filter(messages.get(k), word);
							}
							messages.replace(k, filtered);
						});
					}
				});
				
				always.stream().forEach(word -> {
					if(construct(word).matcher(message).find()) {
						messages.forEach((k, v) -> {
							String filtered = filter(v, word);
							messages.put(k, filtered);
						});
					}
				});
				
				messages.forEach((k, v) -> {
					k.sendMessage(v);
				});
			}
		}.runTask(CubeChat.get());
	}
	
	private String filter(String message, String word) {
		message = message.replaceAll(this.construct(word).pattern(), "****");
		return message;
	}
	
	/* 
	 * This regex almost completely works, but certain combinations will still make it past.
	 * Current bugs:
	 * - b1tch is not caught
	 * - n1gga is not caught
	 * - f@ggot is not caught
	 * - motherfucker will only censor "fucker" because "fuck" comes before "motherfuck" in the config
	 */
	
	
	private Pattern construct(String word) {
		String sequence = "";
		for(String character : word.split("")) {
			sequence += "((" + character + "\\S*)|" + character + "(\\W|\\d|_)*)";
		}
		return Pattern.compile(sequence, Pattern.CASE_INSENSITIVE);
	}
}

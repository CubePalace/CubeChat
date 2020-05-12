package com.cubepalace.cubechat.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.cubepalace.cubechat.util.Filter;
import org.apache.commons.lang.StringEscapeUtils;
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

	private static final Map<String, Pattern> hardCensor = new HashMap<>();
	private static final Map<String, Pattern> softCensor = new HashMap<>();

	static {
		ConfigFile.get().getConfig().getStringList("hardCensor").forEach(w -> {
			addEntry(w, true);
		});
		ConfigFile.get().getConfig().getStringList("softCensor").forEach(w -> {
			addEntry(w, false);
		});
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChatFilter(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		new BukkitRunnable() {
			public void run() {
				// Format the message, before we do anything (since this is the lowest-priority)
				String message = 
						String.format(event.getFormat(), new Object[] { event.getPlayer().getDisplayName(), event.getMessage() });
				
				Bukkit.getServer().getConsoleSender().sendMessage(message);
				
				Map<Player, ChatOptions> recipients = event.getRecipients().stream()
						.collect(Collectors.toMap(k -> k, v -> CubeChat.get().getOptions(v.getUniqueId())));
				Map<Player, String> messages = event.getRecipients().stream()
						.collect(Collectors.toMap(k -> k, v -> message));

				softCensor.forEach((word, regex) -> {
					if(regex.matcher(message).find()) {
						recipients.forEach((k, v) -> {
							String filtered = messages.get(k);
							if(v.hasFilter()) {
								filtered = filter(messages.get(k), word);
							}
							messages.replace(k, filtered);
						});
					}
				});
				
				hardCensor.forEach((word, regex) -> {
					if(regex.matcher(message).find()) {
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
		message = message.replaceAll(getRegex(word).pattern(), "****");
		return message;
	}

	/*
	 * "motherfucker" will censor as "mother****" because "fuck" comes before "motherfuck"
	 * I have tried experimenting with more regex options, but I can't seem to perfect it.
	 */

	private static Pattern construct(String word) {
		StringBuilder sequence = new StringBuilder();
		for(String character : word.split("")) {
			sequence.append('(');
			// Add any necessary character checks
			if(Filter.hasReplacement(character.charAt(0)))
				Filter.getReplacements(character.charAt(0)).forEach(c -> {
					String safe = StringEscapeUtils.escapeJava(String.valueOf(c));
					sequence.append('(').append(safe).append("\\S*)|");
				});
			sequence.append('(').append(character).append("\\S*)|").append(character).append("(\\W|\\d|_)*)");
		}
		return Pattern.compile(sequence.toString(), Pattern.CASE_INSENSITIVE);
	}

	private static void addEntry(String word, boolean hard) {
		if(hard)
			hardCensor.put(word, construct(word));
		else
			softCensor.put(word, construct(word));
	}

	private static Pattern getRegex(String word) {
		return hardCensor.containsKey(word) ? hardCensor.get(word) : softCensor.get(word);
	}
}

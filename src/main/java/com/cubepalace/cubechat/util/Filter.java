package com.cubepalace.cubechat.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.cubepalace.cubechat.CubeChat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class Filter {

	private CubeChat instance;
	private LevenshteinDistance distance;
	private List<String> softCensor;
	private List<String> hardCensor;
	private List<String> whitelist;
	private List<String> suffixes;

	public Filter(CubeChat instance) {
		this.instance = instance;
		distance = new LevenshteinDistance();
		softCensor = instance.getCustomConfig().getStringList("softCensor");
		hardCensor = instance.getCustomConfig().getStringList("hardCensor");
		whitelist = instance.getCustomConfig().getStringList("falsePositives");
		suffixes = new ArrayList<String>();
		suffixes.add("");
		suffixes.add("er");
		suffixes.add("ed");
		suffixes.add("d");
		suffixes.add("ing");
		suffixes.add("s");
		suffixes.add("es");
		suffixes.add("ers");
		suffixes.add("r");
	}

	public void filter() {
		ProtocolManager pm = instance.getPM();
		List<UUID> notFiltered = instance.getNoFilter();
		pm.addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT) {
			@Override
			public void onPacketSending(PacketEvent e) {
				PacketContainer packet = e.getPacket();
				Player p = e.getPlayer();
				WrappedChatComponent chatComp = packet.getChatComponents().read(0);
				String message = chatComp.getJson();
				String newMsg = message;

				newMsg = censor(hardCensor, whitelist, newMsg);

				if (!notFiltered.contains(p.getUniqueId())) {
					newMsg = censor(softCensor, whitelist, newMsg);
				}

				WrappedChatComponent finalMsg = WrappedChatComponent.fromJson(newMsg);
				packet.getChatComponents().write(0, finalMsg);
			}
		});
	}

	public void updateLists() {
		softCensor = instance.getCustomConfig().getStringList("softCensor");
		hardCensor = instance.getCustomConfig().getStringList("hardCensor");
		whitelist = instance.getCustomConfig().getStringList("falsePositives");
	}

	private String censor(List<String> censor, List<String> whitelist, String message) {
		String newMsg = message;
		
		BaseComponent[] components = ComponentSerializer.parse(newMsg);

		List<String> chatMsgs = new ArrayList<String>();
		for (BaseComponent comp : components)
			chatMsgs.add(comp.toLegacyText());
	
		for (String text : chatMsgs) {
			for (String word : text.split(" ")) {
				if (word.length() < 3)
					continue;
				if (isCensored(censor, whitelist, word))
					newMsg = newMsg.replace(word, "****");
			}
		}

		return newMsg;
	}

	private boolean isCensored(List<String> censor, List<String> whitelist, String message) {
		String msg = message.toLowerCase();
		msg = convert(msg);
		int configDistance = instance.getCensorDistance();
		for (String allowed : whitelist) {
			for (String suffix : suffixes) {
				if (msg.equalsIgnoreCase(allowed + suffix)) {
					return false;
				}
			}
		}

		if (msg.length() <= configDistance + 1) {
			for (String swear : censor) {
				for (String suffix : suffixes) {
					if (msg.equals(swear + suffix)) {
						//instance.getLogger().info("1- Positive match for " + swear + suffix + " on \"" + msg + "\"");
						return true;
					}
				}
			}
		} else {
			for (String swear : censor) {
				for (String suffix : suffixes) {
					if (swear.length() <= configDistance * 2 || msg.length() <= configDistance * 2) {
						if (distance.apply(swear + suffix, msg) <= configDistance - 1) {
							//instance.getLogger().info("2 - Positive match for " + swear + suffix + " on \"" + msg + "\"");
							return true;
						}
					} else {
						if (distance.apply(swear + suffix, msg) <= configDistance) {
							//instance.getLogger().info("3 - Positive match for " + swear + suffix + " on \"" + msg + "\"");
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private String convert(String input) {
		String msg = input;
		msg = msg.replace("4", "a");
		msg = msg.replace("@", "a");
		msg = msg.replace("<", "c");
		msg = msg.replace("3", "e");
		msg = msg.replace("1", "i");
		msg = msg.replace("!", "i");
		msg = msg.replace("|", "i");
		msg = msg.replace("$", "s");
		msg = msg.replace("5", "s");
		msg = msg.replace(".", "");
		msg = msg.replace(",", "");
		msg = msg.replace(" ", "");
		msg = msg.replace("/", "");

		return msg;
	}

}

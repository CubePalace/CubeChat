package com.cubepalace.cubechat.listeners;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.cubepalace.cubechat.CubeChat;
import com.cubepalace.cubechat.util.ConfigFile;

public class AdvertisingListener implements Listener {

	private List<String> splits;

	public AdvertisingListener(CubeChat instance) {
		splits = ConfigFile.get().getConfig().getStringList("adsplits");
	}

	@EventHandler
	public void onAdvertising(AsyncPlayerChatEvent e) {
		String msg = e.getMessage();
		String regex;
		if (!e.getPlayer().hasPermission("cubechat.link")) {
			for (String split : splits) {
				regex = "([0-2]?\\d?\\d" + split + "){3}[0-2]?\\d?\\d";
				msg = msg.replaceAll(regex, "*****");
				regex = "((http(s)?|ftp):\\/\\/)?(www" + split + ")?[-a-zA-Z0-9@:%._\\+~#=]{1,256}" + split + "[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
				msg = msg.replaceAll(regex, "*****");
			}

		}

		e.setMessage(msg);
	}
}

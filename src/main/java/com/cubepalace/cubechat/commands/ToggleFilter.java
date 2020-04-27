package com.cubepalace.cubechat.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cubepalace.cubechat.ChatOptions;
import com.cubepalace.cubechat.CubeChat;

public class ToggleFilter implements CommandExecutor {

	private CubeChat instance;

	public ToggleFilter(CubeChat instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("togglefilter")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "The console cannot use this command");
				return true;
			}

			if (!sender.hasPermission("cubechat.togglefilter")) {
				sender.sendMessage(instance.getNoPerm());
				return true;
			}
			
			Player p = (Player) sender;
			UUID uuid = p.getUniqueId();
			ChatOptions options = instance.getOptions(uuid);
			
			boolean filter = options.hasFilter();
			p.sendMessage(ChatColor.GOLD + "Your filter has been " + (!filter ? "en" : "dis") + "abled");
			
			options.setFilter(!options.hasFilter());
			instance.setOptions(uuid, options);
			return true;
		}
		return false;
	}
}

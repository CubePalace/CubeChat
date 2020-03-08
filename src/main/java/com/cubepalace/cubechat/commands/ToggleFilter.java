package com.cubepalace.cubechat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
			boolean filter = instance.getNoFilter().contains(p.getUniqueId());
			p.sendMessage(ChatColor.GOLD + "Your filter has been " + (filter ? "en" : "dis") + "abled");
			
			if (instance.getNoFilter().contains(p.getUniqueId()))
				instance.removeNoFilter(p.getUniqueId());
			else
				instance.addNoFilter(p.getUniqueId());
			return true;
		}
		return false;
	}
}

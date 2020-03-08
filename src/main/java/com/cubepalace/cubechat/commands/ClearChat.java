package com.cubepalace.cubechat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cubepalace.cubechat.CubeChat;

public class ClearChat implements CommandExecutor {

	CubeChat instance;

	public ClearChat(CubeChat instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("clearchat")) {
			if (!sender.hasPermission("cubechat.clearchat")) {
				sender.sendMessage(instance.getNoPerm());
				return true;
			}

			for (Player p : instance.getServer().getOnlinePlayers()) {
				if (p.hasPermission("cubechat.clearchat.exempt")) {
					p.sendMessage(ChatColor.GOLD + "Your chat was not cleared as you are exempt.");
					continue;
				}
				for (int i = 0; i < 500; i++) {
					p.sendMessage("");
				}
			}
			instance.getServer().broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "The chat was cleared by "
						+ (args.length == 1 && args[0].equalsIgnoreCase("-s")
								&& sender.hasPermission("cubechat.clearchat.silent") ? "a staff member"
										: sender.getName()));
			return true;
		}
		return false;
	}
}

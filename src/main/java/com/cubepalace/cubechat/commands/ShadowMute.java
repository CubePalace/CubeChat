package com.cubepalace.cubechat.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cubepalace.cubechat.ChatOptions;
import com.cubepalace.cubechat.CubeChat;

public class ShadowMute implements CommandExecutor {
	
	private CubeChat instance;
	
	public ShadowMute(CubeChat instance) {
		this.instance = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("shadowmute")) {
			if (!sender.hasPermission("cubechat.shadowmute")) {
				sender.sendMessage(instance.getNoPerm());
				return true;
			}
			
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Usage: /shadowmute <player>");
				return true;
			}
			UUID uuid;
			Player target = instance.getServer().getPlayer(args[0]);
			if (target == null) {
				@SuppressWarnings("deprecation")
				OfflinePlayer player = instance.getServer().getOfflinePlayer(args[0]);
				if (!player.hasPlayedBefore()) {
					sender.sendMessage(ChatColor.RED + args[0] + " has not joined the server before");
					return true;
				}
				uuid = player.getUniqueId();
			} else {
				uuid = target.getUniqueId();
			}
			
			ChatOptions options = instance.getOptions(uuid);
			
			sender.sendMessage(ChatColor.GREEN + args[0] + " is " + (options.isShadowMuted() ? "no longer" : "now") + " shadowmuted");
			options.setShadowMuted(!options.isShadowMuted());
			return true;
		}
		return false;
	}
}

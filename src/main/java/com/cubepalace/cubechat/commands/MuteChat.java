package com.cubepalace.cubechat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.cubepalace.cubechat.CubeChat;

public class MuteChat implements CommandExecutor {

	private CubeChat instance;

	public MuteChat(CubeChat instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equals("mutechat")) {
			if (!sender.hasPermission("cubechat.mutechat")) {
				sender.sendMessage(instance.getNoPerm());
				return true;
			}

			instance.toggleMute();
			instance.getServer()
					.broadcastMessage((instance.getChatMuted() == true
							? ChatColor.RED + "" + ChatColor.BOLD + "Chat has been muted by "
							: ChatColor.GREEN + "" + ChatColor.BOLD + "Chat has been unmuted by ")
							+ ((args.length == 1 && args[0].equals("-s")
									&& sender.hasPermission("cubechat.mutechat.silent")) ? "a staff member"
											: sender.getName()));
			return true;
		}
		return false;
	}
}

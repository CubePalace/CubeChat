package com.cubepalace.cubechat.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cubepalace.cubechat.CubeChat;

public class CubeChatCmd implements CommandExecutor {

	private CubeChat instance;

	public CubeChatCmd(CubeChat instance) {
		this.instance = instance;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("cubechat")) {
			if (!sender.hasPermission("cubechat.cmd")) {
				sender.sendMessage(instance.getNoPerm());
				return true;
			}
			if (args.length == 0) {
				showHelp(sender);
				return true;
			}

			if (args[0].equalsIgnoreCase("muteread") && sender.hasPermission("cubechat.mutechat.read")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.RED + "The console cannot use this command");
					return true;
				}
				Player p = (Player) sender;
				UUID uuid = p.getUniqueId();

				p.sendMessage(ChatColor.GOLD + "You will "
						+ (instance.getMutedChatIgnore().contains(uuid) ? "now" : "no longer")
						+ " see player messages while the chat is muted");
				if (instance.getMutedChatIgnore().contains(uuid))
					instance.removeIgnoreMutedChat(uuid);
				else
					instance.addIgnoreMutedChat(uuid);
				return true;
			}else if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("cubechat.reload")) {
				instance.getPlayerFile().reload();
				instance.getConfigFile().reload();
				instance.getFilter().updateLists();;
				sender.sendMessage(ChatColor.GOLD + "CubeChat has been reloaded");
				return true;
			}else if (args[0].equalsIgnoreCase("shadowread") && sender.hasPermission("cubechat.shadowmute.read")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.RED + "The console cannot use this command");
					return true;
				}

				Player p = (Player) sender;
				UUID uuid = p.getUniqueId();
				p.sendMessage(ChatColor.GOLD + "You will "
						+ (instance.getIgnoreShadowmuted().contains(uuid) ? "now" : "no longer")
						+ " messages from shadowmuted players");
				if (instance.getIgnoreShadowmuted().contains(uuid))
					instance.removeIgnoreShadowmuted(uuid);
				else
					instance.addIgnoreShadowmuted(uuid);
				return true;
			} else if (args[0].equalsIgnoreCase("checkfilter") && sender.hasPermission("cubechat.checkfilter")) {
				if (args.length == 1) {
					sender.sendMessage(ChatColor.RED + "Usage: /cubechat checkfilter <player>");
					return true;
				}
				
				Player target = instance.getServer().getPlayer(args[1]);
				UUID uuid;
				if (target == null)
					uuid = instance.getServer().getOfflinePlayer(args[1]).getUniqueId();
				else
					uuid = target.getUniqueId();
				
				sender.sendMessage(ChatColor.GOLD + args[1] + " currently does " + (instance.getNoFilter().contains(uuid) ? "not " : "") + "have their filter enabled");
				return true;
			} else if (args[0].equalsIgnoreCase("shadowmuted") && sender.hasPermission("cubechat.shadowmuted")) {
				if (args.length == 1) {
					sender.sendMessage(ChatColor.RED + "Usage: /cubechat shadowmuted <player>");
					return true;
				}
				
				Player target = instance.getServer().getPlayer(args[1]);
				UUID uuid;
				if (target == null)
					uuid = instance.getServer().getOfflinePlayer(args[1]).getUniqueId();
				else
					uuid = target.getUniqueId();
				
				sender.sendMessage(ChatColor.GOLD + args[1] + " currently is " + (instance.getShadowmuted().contains(uuid) ? "" : "not ") + "shadowmuted");
				return true;
			} else if (args[0].equalsIgnoreCase("forcefilter") && sender.hasPermission("cubechat.forcefilter")) {
				if (args.length < 3) {
					sender.sendMessage(ChatColor.RED + "Usage: /cubechat forcefilter <player> <on|off>");
					return true;
				}
				
				if (!args[2].equalsIgnoreCase("on") && !args[2].equalsIgnoreCase("off")) {
					sender.sendMessage(ChatColor.RED + "Usage: /cubechat forcefilter <player> <on|off>");
					return true;
				}
				
				Player target = instance.getServer().getPlayer(args[1]);
				UUID uuid;
				if (target == null)
					uuid = instance.getServer().getOfflinePlayer(args[1]).getUniqueId();
				else
					uuid = target.getUniqueId();
				
				if (args[2].equalsIgnoreCase("on")) {
					instance.removeNoFilter(uuid);
				} else {
					instance.addNoFilter(uuid);
				}
				
				sender.sendMessage(ChatColor.GOLD + args[1] + "'s filter has been forcibly " + (args[2].equalsIgnoreCase("on") ? "en" : "dis") + "abled");
				return true;
			} else {
				showHelp(sender);
				return true;
			}
		}
		return false;
	}

	private void showHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "--[--- Cube Chat ---]--");
		sender.sendMessage(ChatColor.GOLD
				+ "Note: \"-s\" as an argument hides your name in the broadcast (if you have permission)");
		sender.sendMessage(ChatColor.GOLD + "/cubechat [help]: " + ChatColor.AQUA + "Brings up this help menu");
		if (sender.hasPermission("cubechat.togglefilter"))
			sender.sendMessage(
					ChatColor.GOLD + "/togglefilter: " + ChatColor.AQUA + "Toggles whether or not you see bad words");
		
		if (sender.hasPermission("cubechat.clearchat"))
			sender.sendMessage(ChatColor.GOLD + "/clearchat"
					+ (sender.hasPermission("cubechat.clearchat.silent") ? " [-s]: " : ": ") + ChatColor.AQUA
					+ "Clears the chat");
		
		if (sender.hasPermission("cubechat.mutechat"))
			sender.sendMessage(
					ChatColor.GOLD + "/mutechat" + (sender.hasPermission("cubechat.mutechat.silent") ? " [-s]: " : ": ")
							+ ChatColor.AQUA + "Mutes/unmutes the chat");
		
		if (sender.hasPermission("cubechat.shadowmute"))
			sender.sendMessage(
					ChatColor.GOLD + "/shadowmute <player>: " + ChatColor.AQUA + "Mutes a player without them knowing");
		
		if (sender.hasPermission("cubechat.shadowmuted"))
			sender.sendMessage(ChatColor.GOLD + "/cubechat shadowmuted <player>: " + ChatColor.AQUA
					+ "Tells you whether or not a player is shadowmuted");
		
		if (sender.hasPermission("cubechat.mutechat.read"))
			sender.sendMessage(ChatColor.GOLD + "/cubechat muteread: " + ChatColor.AQUA
					+ "Toggles whether or not you read attempted messages while the chat is muted");
		
		if (sender.hasPermission("cubechat.shadowmute.read"))
			sender.sendMessage(ChatColor.GOLD + "/cubechat shadowread: " + ChatColor.AQUA
					+ "Toggles whether or not you see messages from shadowmuted players");
		
		if (sender.hasPermission("cubechat.checkfilter"))
			sender.sendMessage(ChatColor.GOLD + "/cubechat checkfilter <player>: " + ChatColor.AQUA + "Tells you if a player has their filter on or off");
		
		if (sender.hasPermission("cubechat.forcefilter"))
			sender.sendMessage(ChatColor.GOLD + "/cubechat forcefilter <player> <on|off>: " + ChatColor.AQUA + "Forces a player's filter to on or off");
		
		if (sender.hasPermission("cubechat.reload"))
			sender.sendMessage(ChatColor.GOLD + "/cubechat reload: " + ChatColor.AQUA + "Reloads the plugin");
	}
}

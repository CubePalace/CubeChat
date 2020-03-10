# CubeChat
 A complete and configurable chat management plugin for Minecraft servers

ProtocolLib is required for this plugin to function!

Features:
* Automatically sets player messages to lowercase when they use too many capitals (configurable)
* Automatically shortens messages when flooding the same character (configurable)
* Chat message spam cooldown (configurable)
* Blocks advertising/website link attempts
  * Can configure to deal with people trying to circumvent
* Chat Filter
  * Two censor levels - Hard and Soft
  * Words all configurable
  * Soft censor
    * Allows players to choose if they want to see these words
  * Hard censor
    * Message is censored for all players
  * Whitelist
    * If a message is incorrectly flagged as censored, adding it to the whitelist prevents this
  * Uses censor distance (configurable)
    * Number of characters in a word that can be different than the word set in config to still get censored
    * For example, "big" has a distance of 1 from "bag"
  * Deals with players using other characters to circumvent filter

Commands:
* `/togglefilter`
  * Toggles whether or not you see soft censored words
* `/clearchat`
  * Clears the chat
* `/mutechat`
  * Prevents players from talking in chat
* `/shadowmute`
  * Mutes a player without them knowing they're muted
* `/cubechat`
  * Main command for the plugin
  * No arguments will show help menu

Permissions:
* `cubechat.*`
  * Gives access to everything
* `cubechat.cmd`
  * You can do /cubechat
* `cubechat.clearchat`
  * You can clear the chat
* `cubechat.clearchat.exempt`
  * Your chat won't get cleared
* `cubechat.clearchat.silent`
  * You can use the "-s" argument to clear chat without showing your name
* `cubechat.mutechat`
  * You can mute and unmute the chat
* `cubechat.mutechat.exempt`
  * You can talk while the chat is muted
* `cubechat.mutechat.silent`
  * You can use the "-s" argument to mute the chat without showing your name
* `cubechat.mutechat.read`
  * You can read attempted messages from players while the chat is muted (toggleable)
* `cubechat.shadowmute`
  * You can shadowmute players
* `cubechat.shadowmute.read`
  * You can read attempted messages from shadowmuted players (toggleable)
* `cubechat.togglefilter`
  * You can toggle your filter
* `cubechat.checkfilter`
  * You can check if a player's filter is on or off
* `cubechat.forcefilter`
  * You can force a player's filter to be on or off
* `cubechat.reload`
  * You can reload the plugin
* `cubechat.caps.exempt`
  * You are exempt from the capitals filter
* `cubechat.flood.exempt`
  * You are exempt from the flood filter
* `cubechat.spam.exempt`
  * You won't have a message cooldown
* `cubechat.link`
  * You can post links in chat

Known issues:
* Players can bypass the chat filter by putting spaces within a word

To-Do
* Add more config options
  * No permission message
  * Toggle for filter matches to show in chat
    * Useful to look for false positives

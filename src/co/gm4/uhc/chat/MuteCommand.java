package co.gm4.uhc.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.gm4.uhc.Permission;

/**
 * Mute players so they can't speak in chat.
 * <p>
 * This command will also unmute muted players.
 * <p>
 * Usage: <i>/mute [player]</i>
 * 
 * @author MrSugarCaney
 */
public class MuteCommand implements CommandExecutor {

	/**
	 * List of all players that are muted.
	 */
	private List<UUID> mutedPlayers = new ArrayList<>();

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		/*
		 * UNMUTE ALL PLAYERS
		 */
		if (label.equalsIgnoreCase("unmuteall")) {
			int amount = mutedPlayers.size();

			for (UUID mutedId : mutedPlayers) {
				Player muted = Bukkit.getPlayer(mutedId);
				muted.sendMessage(Broadcast.NOTIFICATION + "You have been unmuted.");
			}
			mutedPlayers.clear();

			sender.sendMessage(Broadcast.SUCCESS_PREFIX + amount + " player"
					+ (amount == 1 ? " has" : "s have") + " been unmuted!");
			return true;
		}

		/*
		 * MUTE PLAYER
		 */
		if (args.length == 0) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + "Usage: /mute <player>");
			return false;
		}

		String playerName = args[0];
		Player player = Bukkit.getPlayer(playerName);
		
		if (player == null) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + playerName + " doesn't exist!");
			return false;
		}

		if (!player.isOnline()) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + playerName + " is not online!");
			return false;
		}

		if (player.hasPermission(Permission.MODERATOR)) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + "You can't mute moderators!");
			return false;
		}

		// Unmute
		if (mutedPlayers.contains(player.getUniqueId())) {
			mutedPlayers.remove(player.getUniqueId());
			sender.sendMessage(Broadcast.SUCCESS_PREFIX + playerName + " has been unmuted!");
			player.sendMessage(Broadcast.NOTIFICATION + "You have been unmuted.");
		}
		// Mute
		else {
			mutedPlayers.add(player.getUniqueId());
			sender.sendMessage(Broadcast.SUCCESS_PREFIX + playerName + " has been muted!");
			player.sendMessage(Broadcast.NOTIFICATION + "You have been muted.");
		}

		return true;
	}

	public synchronized boolean isMuted(Player player) {
		return mutedPlayers.contains(player.getUniqueId());
	}

}

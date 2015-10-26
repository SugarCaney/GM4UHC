package co.gm4.uhc.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.gm4.uhc.Permission;

/**
 * Sends a message to all mods (mods only).
 * <p>
 * Usage: <i>/mod [msg]</i>
 * 
 * @author MrSugarCaney
 */
public class ModCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + "Can't send an empty message!");
			return false;
		}

		StringBuilder sb = new StringBuilder("");
		for (String word : args) {
			sb.append(word).append(" ");
		}
		String message = sb.toString().trim();

		String mail = ">> " + Broadcast.MOD_MAIL_PREFIX + sender.getName() + ": " + message;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission(Permission.MODERATOR)) {
				player.sendMessage(mail);
			}
		}

		return true;
	}

}

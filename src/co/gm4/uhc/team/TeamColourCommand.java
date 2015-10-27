package co.gm4.uhc.team;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.chat.Broadcast;

/**
 * Command that will change the colour and/or accent of the team.
 * <p>
 * Usage: /team &lt;player&gt; &lt;colour&gt; [accent]
 * 
 * @author MrSugarCaney
 */
public class TeamColourCommand implements CommandExecutor {

	/**
	 * Main instance of the plugin.
	 */
	private GM4UHC plugin;

	public TeamColourCommand(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(
					Broadcast.ERROR_PREFIX + "Usage: /teamcolour <player> <colour> [accent]");
			return false;
		}

		// Check input: player name
		Player player = Bukkit.getPlayer(args[0]);
		if (player == null) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + "Player " + args[0] + " doesn't exist!");
			return false;
		}

		// Check input: colour
		ChatColor colour = null;
		try {
			colour = ChatColor.valueOf(args[1].toUpperCase());
		}
		catch (IllegalArgumentException e) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + "Colour " + args[1] + " doesn't exist!");
			return false;
		}

		// Check input: accent
		ChatColor accent = null;
		if (args.length >= 3) {
			if (!args[2].toUpperCase()
					.matches("(ITALIC|BOLD|UNDERLINE|STRIKETHROUGH|MAGIC|NONE)")) {
				sender.sendMessage(
						Broadcast.ERROR_PREFIX + "Accent " + args[2] + " doesn't exist!");
				return false;
			}
			else {
				if (!args[2].equalsIgnoreCase("NONE")) {
					accent = ChatColor.valueOf(args[2].toUpperCase());
				}
			}
		}

		// Set team colour.
		Team team = plugin.getTeamManager().getTeamByPlayer(player);

		if (team == null) {
			sender.sendMessage(Broadcast.ERROR_PREFIX + player.getName() + " is not in a team!");
			return false;
		}

		team.setColour(colour);
		team.setAccent(accent);

		String colourString = colour.name();
		String accentString = (accent == null ? "NONE" : accent.name());

		sender.sendMessage(
				Broadcast.SUCCESS_PREFIX + "Set the colour and accent of " + player.getName()
						+ "'s team to respectively " + colourString + " and " + accentString + ".");
		
		plugin.setTabNames();

		return true;
	}

}

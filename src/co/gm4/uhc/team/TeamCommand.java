package co.gm4.uhc.team;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.chat.Broadcast;

/**
 * Command that will put all the given players into a team.
 * <p>
 * It will also remove the players from any previous team.
 * <p>
 * Usage: /team [player1] [player2] [player3] ...
 * 
 * @author MrSugarCaney
 */
public class TeamCommand implements CommandExecutor {

	/**
	 * Main instance of the plugin.
	 */
	private GM4UHC plugin;

	public TeamCommand(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(
					Broadcast.ERROR_PREFIX + "Usage: /team <player1> [player2] [player3]...");
			return false;
		}

		// Parse players
		Set<Player> players = new HashSet<>();
		for (String name : args) {
			Player player = Bukkit.getPlayer(name);

			if (player == null) {
				sender.sendMessage(Broadcast.ERROR_PREFIX + name + " doesn't exist!");
				return false;
			}

			players.add(player);
		}

		// Remove players from their teams.
		for (Player player : players) {
			plugin.getTeamManager().removeFromTeam(player);
		}

		// Create new team.
		Team team = plugin.getTeamManager().newTeam(players);
		
		StringBuilder sb = new StringBuilder();
		for (Player player : players) {
			sb.append(player.getName());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		
		sender.sendMessage(Broadcast.SUCCESS_PREFIX + "Team #" + team.getId() + " has been created "
				+ "containing the following players: " + sb.toString());

		return true;
	}

}

package co.gm4.uhc.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.Util;
import co.gm4.uhc.team.Team;

/**
 * Class containing all the data about the match.
 * 
 * @author MrSugarCaney
 */
public class Match {

	/**
	 * The main instance of the plugin.
	 */
	private GM4UHC plugin;

	/**
	 * A list of all players competing.
	 */
	private List<UUID> players = new ArrayList<>();

	/**
	 * The state of the match.
	 * <p>
	 * Default: {@link MatchState#LOBBY}
	 */
	private MatchState state = MatchState.LOBBY;

	/**
	 * The amount of seconds that have passed since the start of the match.
	 */
	private long timer = 0L;

	/**
	 * The worldborder of the match world.
	 */
	private WorldBorder border;

	public Match(GM4UHC plugin) {
		this.plugin = plugin;

		try {
			plugin.getServer().getWorld(plugin.getConfig().getString("lobby-name")).setPVP(false);
		}
		catch (NullPointerException e) {
			plugin.getLogger().log(Level.SEVERE, "<!!!> No/wrong lobby world has been set up!");
		}

		try {
			plugin.getServer().getWorld(plugin.getConfig().getString("world-name")).setPVP(false);
		}
		catch (NullPointerException e) {
			plugin.getLogger().log(Level.SEVERE, "<!!!> No/wrong world has been set up!");
		}
	}

	/**
	 * Sets up all the prequisites required for the match.
	 * <p>
	 * e.g. setup the world border.
	 */
	private void setupWorld() {
		int teamCount = plugin.getTeamManager().getTeams().size();
		int teamSpace = plugin.getConfig().getInt("team-plot");

		// Determine world size.
		int size = (int)Math.sqrt(teamCount * teamSpace * teamSpace);

		// Setup world border.
		String worldName = plugin.getConfig().getString("world-name");
		World world = plugin.getServer().getWorld(worldName);
		Location centre = world.getSpawnLocation();
		border = world.getWorldBorder();
		border.setCenter(centre);
		border.setSize(size);
		border.setWarningTime(30);
	}

	/**
	 * Happens everytime a second has passed after the match has started.
	 * 
	 * TODO: Implement.
	 */
	private void tick() {
		timer += 1;

		// Time mark announcement
		int mark = plugin.getConfig().getInt("time-mark");
		if (timer % mark == 0) {
			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + ">> "
					+ ChatColor.GREEN + "Mark " + Util.toCountdownString((timer)));
		}
		
		// Enable PVP at end of grace period.
		int gracePeriod = plugin.getConfig().getInt("grace-period");
		if (timer % gracePeriod == 0) {
			plugin.getServer().getWorld(plugin.getConfig().getString("world-name")).setPVP(true);
			
			if (timer == gracePeriod) {
				Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + ">> "
					+ ChatColor.GREEN + "Grace period has ended!");
			}
		}
	}

	/**
	 * First spreads the players, then starts the countdown and the game.
	 * <p>
	 * When the countdown has ended, {@link Match#starts()} will start.
	 */
	public void startCountdown() {
		// Prepare stuff.
		state = MatchState.PREPARING;
		plugin.getServer().getWorld(plugin.getConfig().getString("world-name")).setPVP(false);
		spreadPlayers();
		
		// Announce countdown.
		int countdown = plugin.getConfig().getInt("countdown");
		broadcastTime(countdown, countdown);

		if (countdown > 60) {
			broadcastTime(countdown, 60);
		}

		if (countdown > 30) {
			broadcastTime(countdown, 30);
		}

		if (countdown > 15) {
			broadcastTime(countdown, 15);
		}

		if (countdown > 10) {
			broadcastTime(countdown, 10);
		}

		if (countdown > 5) {
			broadcastTime(countdown, 5);
		}

		if (countdown > 4) {
			broadcastTime(countdown, 4);
		}

		if (countdown > 3) {
			broadcastTime(countdown, 3);
		}

		if (countdown > 2) {
			broadcastTime(countdown, 2);
		}

		if (countdown > 1) {
			broadcastTime(countdown, 1);
		}

		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			start();
			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "START "
					+ ChatColor.GREEN + "May the odds be ever in your favour!");
		}, countdown * 20);
		
	}

	private void broadcastTime(int countdown, int seconds) {
		String template = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "COUNTDOWN "
				+ ChatColor.GREEN + "%t until start!";
		long delay = (countdown - seconds) * 20L;
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			Bukkit.broadcastMessage(template.replace("%t", Util.toCountdownString(seconds)));
		} , delay);
	}

	/**
	 * TODO: Implement this method ^.^
	 */
	public void start() {
		setupWorld();
		setGameRules();
		
		for (Team team : plugin.getTeamManager().getTeams()) {
			players.addAll(team.getPlayers());
		}
		
		state = MatchState.RUNNING;
		
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> tick(), 20L, 20L);
	}
	
	/**
	 * Setup all the gamerules for UHC.
	 */
	public void setGameRules() {
		World world = plugin.getServer().getWorld(plugin.getConfig().getString("world-name"));
		world.setGameRuleValue("commandBlockOutput", "false");
		world.setGameRuleValue("keepInventory", "false");
		world.setGameRuleValue("naturalRegeneration", "false");
	}

	/**
	 * TODO: implement.
	 */
	public void spreadPlayers() {
		Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "SPREAD "
				+ ChatColor.GREEN + "Player spreading has started!");
	}

	/**
	 * Get the time in seconds that have passed after the game has started.
	 * 
	 * @return The amount of seconds after the start of the match.
	 */
	public long time() {
		return timer;
	}

	/**
	 * Returns the amount of players that are in the match competing.
	 * 
	 * @return The amount of players competing.
	 */
	public int count() {
		return players.size();
	}

	/**
	 * Removes a player from the match.
	 * 
	 * @param player
	 *            The player to remove from the match.
	 * @return <i>true</i> if the player was competing in the match,
	 *         <i>false</i> if not.
	 */
	public boolean remove(Player player) {
		return players.remove(player);
	}

	/**
	 * Check if a player is competing in the match.
	 * <p>
	 * Match must have started (state == RUNNING).
	 * 
	 * @param player
	 *            the player to check for.
	 * @return <i>true</i> if the player competes, <i>false</i> if not.
	 */
	public boolean isCompeting(Player player) {
		return players.contains(player.getUniqueId());
	}

	/**
	 * Checks if the grace period is still active.
	 * 
	 * @return <i>true</i> when in the grace period, <i>false</i> if not.
	 */
	public boolean isGracePeriod() {
		int graceTime = plugin.getConfig().getInt("grace-period");
		return timer < graceTime;
	}
	
	public List<UUID> getPlayers() {
		return players;
	}

	public MatchState getState() {
		return state;
	}

}

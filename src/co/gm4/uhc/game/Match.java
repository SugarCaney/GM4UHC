package co.gm4.uhc.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
	 * Tracks how long players have been offline.
	 * <p>
	 * Longer than 5 minutes results into death.
	 */
	private Map<UUID, Integer> offline = new HashMap<>();

	/**
	 * Tracks the amount of warnings.
	 */
	private Map<UUID, Integer> warnings = new HashMap<>();

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
	 * Tracks the height of the lava.
	 * <p>
	 * Default: <i>0</i>
	 */
	private int lavaLevel = 0;

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
		// Determine world size.
		int size = getSize();

		// Setup world border.
		String worldName = plugin.getConfig().getString("world-name");
		World world = plugin.getServer().getWorld(worldName);
		Location centre = world.getSpawnLocation();
		border = world.getWorldBorder();
		border.setCenter(centre);
		border.setSize(size);
		border.setWarningTime(30);
		
		world.setTime(0);
	}

	/**
	 * Calculates the size of the arena.
	 */
	public int getSize() {
		int teamCount = plugin.getTeamManager().getTeams().size();
		int teamSpace = plugin.getConfig().getInt("team-plot");
		return (int)Math.sqrt(teamCount * teamSpace * teamSpace);
	}

	/**
	 * Happens everytime a second has passed after the match has started.
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
		int shrinkTime = plugin.getConfig().getInt("shrink-time");
		if (timer % gracePeriod == 0) {
			plugin.getServer().getWorld(plugin.getConfig().getString("world-name")).setPVP(true);

			if (timer == gracePeriod) {
				Bukkit.broadcastMessage(
						ChatColor.DARK_GREEN + "" + ChatColor.BOLD + ">> " + ChatColor.GREEN
								+ "Grace period has ended and the border has started shrinking!");

				border.setSize(plugin.getConfig().getInt("border-end"), shrinkTime);
			}
		}

		// Offline players
		for (UUID playerId : offline.keySet()) {
			OfflinePlayer player = Bukkit.getOfflinePlayer(playerId);
			if (player.isOnline()) {
				continue;
			}

			int value = offline.get(playerId);
			offline.put(playerId, value + 1);

			if (value + 1 > plugin.getConfig().getInt("offline-time")) {
				Team team = plugin.getTeamManager().getTeamByUUID(playerId);

				if (team != null) {
					team.die(playerId, plugin);
					Bukkit.broadcastMessage(team.getChatColours() + player.getName()
							+ ChatColor.YELLOW + " has lost due to offline time.");
					offline.remove(playerId);
				}
			}
		}

		// Rise lava
		int lavaTime = plugin.getConfig().getInt("lava-time");
		int lavaTop = plugin.getConfig().getInt("lava-max");
		int startLava = gracePeriod + shrinkTime;
		int secondsPerLava = (int)((float)lavaTime / (float)lavaTop);

		if (timer == startLava) {
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "LAVA "
					+ ChatColor.GOLD + "Lava has started rising.");
		}

		if (timer > startLava) {
			if ((timer - startLava) % secondsPerLava == 0
					&& startLava < plugin.getConfig().getInt("lava-max")) {
				lavaLevel++;

				Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "LAVA "
						+ ChatColor.GOLD + "The lava has risen to level " + lavaLevel + ".");
			}

			for (UUID playerId : players) {
				Player player = Bukkit.getPlayer(playerId);
				Location loc = player.getLocation();
				if (loc.getY() <= lavaLevel) {
					player.setFireTicks(25);
					player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "LAVA "
							+ ChatColor.GOLD + "You are being damaged by rising lava.");
				}
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
		setupWorld();
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
			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + ">> "
					+ ChatColor.GREEN + "The grace period will last "
					+ Util.toCountdownString(plugin.getConfig().getInt("grace-period")));
		} , countdown * 20);

		// Clear stuff and add players to the matchlist.
		for (Team team : plugin.getTeamManager().getTeams()) {
			for (UUID playerId : team.getPlayers()) {
				Player player = Bukkit.getPlayer(playerId);
				player.setHealth(20);
				player.setFoodLevel(20);
				player.setSaturation(20);
				player.setExhaustion(20);
				player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,
						200 + countdown * 20, 100));
				player.setGameMode(GameMode.SURVIVAL);
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				player.setExp(0);
				player.setLevel(0);
				player.setFireTicks(0);
				player.setFlying(false);
				players.add(playerId);
			}
		}
	}

	/**
	 * Show a countdown message.
	 * 
	 * @param countdown
	 *            The length of the countdown in seconds.
	 * @param seconds
	 *            The amount of seconds left.
	 */
	private void broadcastTime(int countdown, int seconds) {
		String template = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "COUNTDOWN "
				+ ChatColor.GREEN + "%t until start!";
		long delay = (countdown - seconds) * 20L;
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			Bukkit.broadcastMessage(template.replace("%t", Util.toCountdownString(seconds)));
		} , delay);
	}

	/**
	 * Starts the match.
	 * <p>
	 * Finalises what startCountdown() does.
	 */
	public void start() {
		setGameRules();

		// Again a heal.
		for (Team team : plugin.getTeamManager().getTeams()) {
			for (UUID playerId : team.getPlayers()) {
				Player player = Bukkit.getPlayer(playerId);
				if (player == null) {
					continue;
				}

				player.setHealth(20);
				player.setFoodLevel(20);
				player.setSaturation(20);
				player.setExhaustion(20);
			}
		}

		state = MatchState.RUNNING;
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> tick(), 20L, 20L);
	}

	/**
	 * Setup all the gamerules for UHC. Plus the time hehe.
	 */
	public void setGameRules() {
		World world = plugin.getServer().getWorld(plugin.getConfig().getString("world-name"));
		world.setGameRuleValue("commandBlockOutput", "false");
		world.setGameRuleValue("keepInventory", "false");
		world.setGameRuleValue("naturalRegeneration", "false");
		world.setTime(20);
	}

	/**
	 * Spreads all the teams.
	 * <p>
	 * Players will be spread over an area with size of getSize()*getSize().
	 * This method will also place obsidian and lily pads on/at water/lava to
	 * make sure nobody gets hurt on the start. The players will be spread with
	 * a certain minimum distance as set in the config (distance-threshold) in
	 * percentage of the team plot.
	 */
	public void spreadPlayers() {
		// Calculating positions.
		Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "SPREAD "
				+ ChatColor.GREEN + "Calculating positions. This may take a while.");

		Random ran = new Random();
		int size = getSize();
		int distanceThreshold = (int)(plugin.getConfig().getDouble("distance-threshold")
				* plugin.getConfig().getInt("team-plot"));
		Location centre = plugin.getServer().getWorld(plugin.getConfig().getString("world-name"))
				.getSpawnLocation();
		int maxX = (int)(centre.getX() + size / 2) - 5;
		int minX = (int)(centre.getX() - size / 2) + 5;
		int maxZ = (int)(centre.getZ() + size / 2) - 5;
		int minZ = (int)(centre.getZ() - size / 2) + 5;
		World world = centre.getWorld();

		List<Team> teams = plugin.getTeamManager().getTeams();
		HashMap<Team, Location> places = new HashMap<>();
		boolean correctLocations = false;

		while (!correctLocations) {
			// Reset places.
			places.clear();

			// Randomly pick location.
			for (Team team : teams) {
				double x = Util.randomRange(minX, maxX, ran) + 0.5;
				double z = Util.randomRange(minZ, maxZ, ran) + 0.5;
				int y = world.getHighestBlockYAt((int)x, (int)z) + 1;
				Location loc = new Location(centre.getWorld(), x, y, z);
				places.put(team, loc);
			}

			// Check if places are valid.
			correctLocations = true;
			placeCheck: for (Entry<Team, Location> ent : places.entrySet()) {
				for (Team team : teams) {
					if (team.equals(ent.getKey())) {
						continue;
					}

					Location loc1 = ent.getValue();
					Location loc2 = places.get(team);
					double dist = Util.distance(loc1, loc2);

					if (dist < distanceThreshold) {
						correctLocations = false;
						break placeCheck;
					}
				}
			}
		}

		// Preparing teleport locations.
		Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "SPREAD "
				+ ChatColor.GREEN + "Preparing teleport locations.");

		for (Location loc : places.values()) {
			Location check = new Location(loc.getWorld(), loc.getX(), loc.getY() - 2, loc.getZ());
			if (check.getBlock().getType() == Material.WATER
					|| check.getBlock().getType() == Material.STATIONARY_WATER) {
				loc.clone().add(0, -1, 0).getBlock().setType(Material.WATER_LILY);
			}

			if (check.getBlock().getType() == Material.LAVA
					|| check.getBlock().getType() == Material.STATIONARY_LAVA) {
				check.getBlock().setType(Material.OBSIDIAN);
			}

			if (loc.clone().add(0, -2, 0).getBlock().getType() == Material.LAVA
					|| loc.clone().add(0, -2, 0).getBlock().getType() == Material.STATIONARY_LAVA) {
				loc.clone().add(0, -2, 0).getBlock().setType(Material.OBSIDIAN);
			}

			if (loc.clone().add(0, -1, 0).getBlock().getType() == Material.LAVA
					|| loc.clone().add(0, -1, 0).getBlock().getType() == Material.STATIONARY_LAVA) {
				loc.clone().add(0, -1, 0).getBlock().setType(Material.OBSIDIAN);
			}

			if (check.getBlock().getType() == Material.CACTUS) {
				check.clone().add(0, 1, 0).getBlock().setType(Material.CARPET);
			}

			if (loc.clone().add(0, -1, 0).getBlock().getType() == Material.CACTUS) {
				loc.getBlock().setType(Material.CARPET);
			}
		}

		// Teleporting
		Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "SPREAD "
				+ ChatColor.GREEN + "Teleporting players.");

		for (Entry<Team, Location> ent : places.entrySet()) {
			for (UUID playerId : ent.getKey().getPlayers()) {
				Player player = Bukkit.getPlayer(playerId);
				player.teleport(ent.getValue());
			}
		}
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

	public Map<UUID, Integer> getWarnings() {
		return warnings;
	}

	public Map<UUID, Integer> getOffline() {
		return offline;
	}

	public List<UUID> getPlayers() {
		return players;
	}

	public MatchState getState() {
		return state;
	}

}

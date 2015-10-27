package co.gm4.uhc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import co.gm4.uhc.chat.ChatListener;
import co.gm4.uhc.chat.ModCommand;
import co.gm4.uhc.chat.MuteCommand;
import co.gm4.uhc.chat.ShoutCommand;
import co.gm4.uhc.chat.SilenceCommand;
import co.gm4.uhc.gameplay.PlayerJoinListener;
import co.gm4.uhc.gameplay.PotionBanListener;
import co.gm4.uhc.team.FriendlyFire;
import co.gm4.uhc.team.RemoveCommand;
import co.gm4.uhc.team.Team;
import co.gm4.uhc.team.TeamColourCommand;
import co.gm4.uhc.team.TeamCommand;
import co.gm4.uhc.team.TeamListCommand;
import co.gm4.uhc.team.TeamManager;
import co.gm4.uhc.team.TeamlessCommand;

/**
 * @author MrSugarCaney
 */
public class GM4UHC extends JavaPlugin {

	/**
	 * Instance managing the creation and storage of teams.
	 */
	private TeamManager teamManager = new TeamManager();

	/**
	 * Handling the muting of players.
	 */
	private MuteCommand mute = new MuteCommand();

	/**
	 * Handling the ability to silence chat.
	 */
	private SilenceCommand silence = new SilenceCommand();

	@Override
	public void onEnable() {
		saveDefaultConfig();
		registerListeners();
		registerCommands();
		registerTasks();

		setTabNames();

		getLogger().info("Plugin has been enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().info("Plugin has been disabled!");
	}

	/**
	 * Registers all the command executors.
	 */
	public void registerCommands() {
		getCommand("shout").setExecutor(new ShoutCommand());
		getCommand("mod").setExecutor(new ModCommand());
		getCommand("mute").setExecutor(mute);
		getCommand("unmuteall").setExecutor(mute);
		getCommand("silence").setExecutor(silence);
		getCommand("team").setExecutor(new TeamCommand(this));
		getCommand("teamcolour").setExecutor(new TeamColourCommand(this));
		getCommand("colours").setExecutor(new Help());
		getCommand("remove").setExecutor(new RemoveCommand(this));
		getCommand("teamlist").setExecutor(new TeamListCommand(this));
		getCommand("teamless").setExecutor(new TeamlessCommand(this));
	}

	/**
	 * Registers all the listeners that should listen to events.
	 */
	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PotionBanListener(), this);
		pm.registerEvents(new ChatListener(this), this);
		pm.registerEvents(new FriendlyFire(this), this);
		pm.registerEvents(new PlayerJoinListener(this), this);
	}

	/**
	 * Registers all the reoccuring events.
	 */
	public void registerTasks() {
		BukkitScheduler scheduler = getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, () -> setTabNames(), 0L, 20L);
	}

	/**
	 * Sets the tab name of all players.
	 */
	public void setTabNames() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			String name = "";
			Team team = teamManager.getTeamByPlayer(player);
			ChatColor colour = ChatColor.WHITE;

			if (player.hasPermission(Permission.MODERATOR)) {
				if (getConfig().isSet("colours." + player.getName())) {
					colour = ChatColor
							.valueOf(getConfig().getString("colours." + player.getName()));
				}
				else {
					colour = ChatColor.GOLD;
				}

				name += (team == null ? colour : team.getColour()) + "" + ChatColor.BOLD + ""
						+ ChatColor.ITALIC + "MOD ";
			}

			if (team == null) {
				name += colour + player.getName();

				if (player.hasPermission(Permission.MODERATOR)) {
					name += ChatColor.WHITE + "" + ChatColor.BOLD + " SP";
				}
			}
			else {
				name += team.getChatColours() + player.getName();

				// Add health.
				if (player.getGameMode() == GameMode.SURVIVAL) {
					int health = (int)player.getHealth();
					name += getHeartColor(health) + " ❤" + ChatColor.YELLOW + health;
				}
			}

			player.setPlayerListName(name);
		}
	}

	/**
	 * Determines what colour the tab-heart should have given a specific health
	 * amount.
	 * 
	 * @param health
	 *            The amount of health.
	 * @return The corresponding ChatColor.
	 */
	private ChatColor getHeartColor(int health) {
		if (health >= 18) {
			return ChatColor.DARK_GREEN;
		}
		else if (health >= 14) {
			return ChatColor.GREEN;
		}
		else if (health >= 10) {
			return ChatColor.YELLOW;
		}
		else if (health >= 7) {
			return ChatColor.GOLD;
		}
		else if (health >= 4) {
			return ChatColor.RED;
		}
		else {
			return ChatColor.DARK_RED;
		}
	}
	
	public TeamManager getTeamManager() {
		return teamManager;
	}

	public SilenceCommand getSilence() {
		return silence;
	}

	public MuteCommand getMute() {
		return mute;
	}

}

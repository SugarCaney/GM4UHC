package co.gm4.uhc;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import co.gm4.uhc.chat.ChatListener;
import co.gm4.uhc.gameplay.PotionBanListener;
import co.gm4.uhc.team.TeamManager;

/**
 * @author MrSugarCaney
 */
public class GM4UHC extends JavaPlugin {

	/**
	 * Instance managing the creation and storage of teams.
	 */
	private TeamManager teamManager = new TeamManager();

	@Override
	public void onEnable() {
		saveDefaultConfig();
		registerListeners();

		getLogger().info("Plugin has been enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().info("Plugin has been disabled!");
	}

	public TeamManager getTeamManager() {
		return teamManager;
	}

	/**
	 * Registers all the listeners that should listen to events.
	 * 
	 * @param pm
	 *            The plugin manager of the plugin.
	 */
	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PotionBanListener(), this);
		pm.registerEvents(new ChatListener(this), this);
	}

}

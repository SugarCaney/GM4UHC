package co.gm4.uhc;

import org.bukkit.plugin.java.JavaPlugin;

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

		System.out.println("[GM4UHC] plugin has been enabled!");
	}

	@Override
	public void onDisable() {
		System.out.println("[GM4UHC] plugin has been disabled!");
	}
	
	public TeamManager getTeamManager() {
		return teamManager;
	}

}

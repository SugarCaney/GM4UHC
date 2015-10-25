package co.gm4.uhc;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author MrSugarCaney
 */
public class GM4UHC extends JavaPlugin {

	@Override
	public void onEnable() {
		saveDefaultConfig();

		System.out.println("[GM4UHC] plugin has been enabled!");
	}

	@Override
	public void onDisable() {
		System.out.println("[GM4UHC] plugin has been disabled!");
	}

}

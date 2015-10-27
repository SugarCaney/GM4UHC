package co.gm4.uhc.gameplay;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import co.gm4.uhc.GM4UHC;

/**
 * Listens to players joining the game.
 * 
 * @author MrSugarCaney
 */
public class PlayerJoinListener implements Listener {

	/**
	 * The main plugin instance.
	 */
	private GM4UHC plugin;
	
	public PlayerJoinListener(GM4UHC plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.setTabNames();
	}
	
}

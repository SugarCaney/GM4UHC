package co.gm4.uhc.team;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import co.gm4.uhc.GM4UHC;

/**
 * Listener cancelling friendly fire.
 * 
 * @author MrSugarCaney
 */
public class FriendlyFire implements Listener {

	/**
	 * Main plugin instance.
	 */
	private GM4UHC plugin;

	public FriendlyFire(GM4UHC plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPVP(EntityDamageByEntityEvent event) {
		
	}

}

package co.gm4.uhc.game;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import co.gm4.uhc.GM4UHC;

/**
 * Participants will be frozen when waiting during the countdown.
 * 
 * @author MrSugarCaney
 */
public class CountdownFreeze implements Listener {
	
	/**
	 * The main plugin instance.
	 */
	private GM4UHC plugin;

	public CountdownFreeze(GM4UHC plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (plugin.getMatch().getState() != MatchState.PREPARING) {
			return;
		}
		
		if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
			return;
		}
		
		Location from = event.getFrom();
		Location to = event.getTo();
		to.setX(from.getX());
		to.setZ(from.getZ());
		event.setTo(to);
	}

}

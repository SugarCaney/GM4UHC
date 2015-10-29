package co.gm4.uhc.game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.Permission;

/**
 * Prevents players breaking, placing and interacting with block.
 * 
 * @author MrSugarCaney
 */
public class GriefProtection implements Listener {

	/**
	 * The main plugin instance.
	 */
	private GM4UHC plugin;

	public GriefProtection(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (player.hasPermission(Permission.MODERATOR)) {
			return;
		}

		if (plugin.getMatch().getState() == MatchState.RUNNING
				&& plugin.getMatch().isCompeting(player)) {
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();

		if (player.hasPermission(Permission.MODERATOR)) {
			return;
		}

		if (plugin.getMatch().getState() == MatchState.RUNNING
				&& plugin.getMatch().isCompeting(player)) {
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		if (player.hasPermission(Permission.MODERATOR)) {
			return;
		}

		if (plugin.getMatch().getState() == MatchState.RUNNING
				&& plugin.getMatch().isCompeting(player)) {
			return;
		}
		
		event.setCancelled(true);
	}

}

package co.gm4.uhc.game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import co.gm4.uhc.GM4UHC;
import co.gm4.uhc.Permission;

/**
 * Prevents players breaking, placing and interacting with blocks.
 * <p>
 * It also helps feeding everyone :3
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
	public void onHunger(FoodLevelChangeEvent event) {
		if (plugin.getMatch().getState() != MatchState.RUNNING) {
			event.setCancelled(true);

			if (event.getEntity() instanceof Player) {
				((Player)event.getEntity()).setSaturation(20);
				((Player)event.getEntity()).setFoodLevel(20);
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}

		if (plugin.getMatch().getState() != MatchState.RUNNING) {
			event.setCancelled(true);
		}
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

package co.gm4.uhc.team;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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
		Projectile proj = null;
		
		if (event.getDamager() instanceof Projectile) {
			proj = (Projectile)event.getDamager();
		}
		
		if (!(event.getDamager() instanceof Player) && proj == null) {
			return;
		}
		
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		
		Player damager = null;
		if (proj.getShooter() instanceof Player) {
			damager = (Player)proj.getShooter();
		}
		else {
			if (event.getDamager() instanceof Player) {
				damager = (Player)event.getDamager();
			}
			else {
				return;
			}
		}
		
		Player victim = (Player)event.getEntity();
		TeamManager tm = plugin.getTeamManager();

		tm.parseTeams(plugin.getConfig());
		
		if (tm.getTeamByPlayer(damager).equals(tm.getTeamByPlayer(victim))) {
			event.setCancelled(true);
		}
	}

}

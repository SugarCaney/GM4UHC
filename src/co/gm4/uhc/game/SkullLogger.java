package co.gm4.uhc.game;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import co.gm4.uhc.GM4UHC;

/**
 * Logs all player skulls that get picked up by players to the console.
 * 
 * @author MrSugarCaney
 */
public class SkullLogger implements Listener {

	/**
	 * Main plugin instance.
	 */
	private GM4UHC plugin;

	public SkullLogger(GM4UHC plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPickupHead(PlayerPickupItemEvent event) {
		ItemStack is = event.getItem().getItemStack();
		if (is.getType() != Material.SKULL_ITEM) {
			return;
		}

		SkullMeta meta = (SkullMeta)is.getItemMeta();
		String owner = meta.getOwner();

		plugin.getLogger().info("SKULL-PICKUP: " + event.getPlayer().getName() + " picked up "
				+ owner + "'s skull.");
	}

}

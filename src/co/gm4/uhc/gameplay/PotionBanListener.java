package co.gm4.uhc.gameplay;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Replacing ghast tear drops with 3 gold ingots and replacing glowstone dust by
 * glowstone blocks. Ghasts will also between 0 and 3 nuggets. If there is no
 * ghast tear, just 1 gold ingot will be dropped.
 * 
 * @author MrSugarCaney
 */
public class PotionBanListener implements Listener {

	private Random ran;

	public PotionBanListener() {
		ran = new Random();
	}

	// Replace ghast tear by 3 gold ingots.
	@EventHandler
	public void onGhastDies(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof Ghast)) {
			return;
		}

		boolean removed = event.getDrops().removeIf(is -> is.getType() == Material.GHAST_TEAR);

		event.getDrops().add(new ItemStack(Material.GOLD_INGOT, removed ? 3 : 1));
		event.getDrops().add(new ItemStack(Material.GOLD_NUGGET, ran.nextInt(4)));
	}

	// Prevent glowstone blocks from dropping glowstone dust. It will drop
	// regular glowstone blocks instead. Ignore gamemode 1.
	@EventHandler
	public void onGlowstoneBreak(BlockBreakEvent event) {
		if (event.getBlock().getType() != Material.GLOWSTONE) {
			return;
		}
		
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}

		Block block = event.getBlock();
		event.setCancelled(true);
		block.setType(Material.AIR);
		block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.GLOWSTONE, 1));
	}

}

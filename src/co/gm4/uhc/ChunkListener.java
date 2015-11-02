package co.gm4.uhc;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

/**
 * Disables chunk loading.
 * <p>
 * Should only be registered when chunk loading is turned off.
 * 
 * @author MrSugarCaney
 */
public class ChunkListener implements Listener {

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		if (event.isNewChunk()) {
			Chunk chunk = event.getChunk();
			chunk.unload(false, false);
		}
	}
	
}

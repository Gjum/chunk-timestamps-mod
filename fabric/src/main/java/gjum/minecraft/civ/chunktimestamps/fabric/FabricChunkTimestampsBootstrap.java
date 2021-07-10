package gjum.minecraft.civ.chunktimestamps.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FabricChunkTimestampsBootstrap implements ModInitializer, ClientChunkEvents.Load {
	private static final Logger LOGGER = LogManager.getLogger();

	private final FabricChunkTimestampsMod mod;

	public FabricChunkTimestampsBootstrap() {
		this.mod = new FabricChunkTimestampsMod();

		ClientChunkEvents.CHUNK_LOAD.register(this);
	}

	@Override
	public void onInitialize() {
		ClientLifecycleEvents.CLIENT_STARTED.register(e -> mod.enable());
	}

	@Override
	public void onChunkLoad(ClientLevel world, LevelChunk chunk) {
		try {
			mod.onChunkLoad(chunk.getPos());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

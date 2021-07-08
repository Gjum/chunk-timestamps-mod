package gjum.minecraft.civ.chunktimestamps.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FabricChunkTimestampsBootstrap implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger();

	private final FabricChunkTimestampsMod mod;

	public FabricChunkTimestampsBootstrap() {
		this.mod = new FabricChunkTimestampsMod();
	}

	@Override
	public void onInitialize() {
		ClientLifecycleEvents.CLIENT_STARTED.register(e -> mod.enable());
	}
}

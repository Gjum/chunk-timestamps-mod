package gjum.minecraft.civ.chunktimestamps.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("chunktimestamps")
public class ForgeChunkTimestampsBootstrap {
	private static final Logger LOGGER = LogManager.getLogger();

	private final ForgeChunkTimestampsMod mod;

	public ForgeChunkTimestampsBootstrap() {
		this.mod = new ForgeChunkTimestampsMod();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void clientSetup(FMLClientSetupEvent event) {
		this.mod.enable();
	}

	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load chunk) {
		try {
			mod.onChunkLoad(chunk.getChunk().getPos());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

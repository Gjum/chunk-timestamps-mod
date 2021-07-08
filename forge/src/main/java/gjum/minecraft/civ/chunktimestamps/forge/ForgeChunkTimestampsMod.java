package gjum.minecraft.civ.chunktimestamps.forge;

import gjum.minecraft.civ.chunktimestamps.AbstractChunkTimestampsMod;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ForgeChunkTimestampsMod extends AbstractChunkTimestampsMod {

	@Override
	public void registerKeyBinding(net.minecraft.client.KeyMapping mapping) {
		ClientRegistry.registerKeyBinding(mapping);
	}
}

package gjum.minecraft.civ.chunktimestamps.fabric;

import gjum.minecraft.civ.chunktimestamps.common.AbstractChunkTimestampsMod;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class FabricChunkTimestampsMod extends AbstractChunkTimestampsMod {
	@Override
	public void registerKeyBinding(KeyMapping mapping) {
		KeyBindingHelper.registerKeyBinding(mapping);
	}
}

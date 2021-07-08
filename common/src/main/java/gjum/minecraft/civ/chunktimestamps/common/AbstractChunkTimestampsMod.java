package gjum.minecraft.civ.chunktimestamps.common;

import net.minecraft.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractChunkTimestampsMod {

	private static AbstractChunkTimestampsMod INSTANCE;
	private static final Logger LOGGER = LogManager.getLogger();

	public AbstractChunkTimestampsMod() {
		int version = Minecraft.getInstance().getGame().getVersion().getProtocolVersion();

		if (INSTANCE == null) {
			INSTANCE = this;
		} else {
			throw new IllegalStateException("AbstractChunkTimestampsMod initialised twice");
		}
	}

	public final void enable() {
		Options options = Minecraft.getInstance().options;
	}

	public abstract void registerKeyBinding(KeyMapping mapping);
}

package gjum.minecraft.civ.chunktimestamps.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.world.level.ChunkPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.util.*;

public abstract class AbstractChunkTimestampsMod {

	private static AbstractChunkTimestampsMod INSTANCE;
	private static final Logger LOGGER = LogManager.getLogger("ChunkTimestampsMod");

	public long privacyMs = 7 * 24 * 60 * 60 * 1000; // week

	public AbstractChunkTimestampsMod() {
		if (INSTANCE == null) {
			INSTANCE = this;
		} else {
			throw new IllegalStateException("AbstractChunkTimestampsMod initialised twice");
		}
	}

	public final void enable() {
		// TODO do init stuff
	}

	public void onChunkLoad(ChunkPos pos) {
		LOGGER.info("onChunkLoad: " + pos);
		updateCurrentTimension();
		if (currentTimension == null) return;
		final long timestamp = System.currentTimeMillis() / privacyMs * privacyMs;
		currentTimension.updateTimestamp(pos, timestamp);
		Optional.of(currentTimension).ifPresent(o -> o.updateTimestamp(pos, timestamp));
	}

	private Timension currentTimension;

	private Timension updateCurrentTimension() {
		final Minecraft mc = Minecraft.getInstance();
		final ServerData currentServer = mc.getCurrentServer();
		if (currentServer == null) {
			return currentTimension = null;
		}
		final String serverName = currentServer.ip;
		final ClientLevel level = mc.level;
		if (level == null) {
			return currentTimension = null;
		}
		final String subWorldName = level.dimension().toString();

		if (currentTimension != null
				&& Objects.equals(currentTimension.serverName, serverName)
				&& Objects.equals(currentTimension.subWorldName, subWorldName)
		) {
			return currentTimension;
		}

		Timension timension = new Timension(serverName, subWorldName);
		LOGGER.info("server: " + serverName
				+ " supWorld: " + subWorldName
				+ " path: " + timension.path);

		if (!timension.path.toFile().isDirectory()) {
			// voxelmap hasn't created this directory (yet?)
			return currentTimension = null;
		}

		return currentTimension = timension;
	}

	/**
	 * we reuse ChunkPos as tile pos
	 */
	static class Timension {
		public final String serverName;
		public final String subWorldName;
		public final Path path;

		private HashMap<ChunkPos, long[]> tiles = new HashMap<>();

		Timension(String serverName, String subWorldName) {
			this.serverName = serverName;
			this.subWorldName = subWorldName;
			final String mcRoot = Minecraft.getInstance().gameDirectory.getAbsolutePath();
			this.path = Paths.get(mcRoot, "voxelmap", "cache", serverName, subWorldName).toAbsolutePath();
		}

		private static String fileName(ChunkPos tilePos) {
			return tilePos.x + "," + tilePos.z + ".timestamps";
		}

		void updateTimestamp(ChunkPos pos, long timestamp) {
			final ChunkPos tilePos = new ChunkPos(pos.x >> 4, pos.z >> 4);
			final long[] chunkTimestamps = tiles.computeIfAbsent(tilePos, this::readTileTimestampsFile);
			final int chunkNr = pos.x & 0xf + 16 * pos.z & 0xf;
			chunkTimestamps[chunkNr] = timestamp;
			writeTileTimestampsFile(tilePos, chunkTimestamps);
		}

		private long[] readTileTimestampsFile(ChunkPos tilePos) {
			try {
				final byte[] bytes = Files.readAllBytes(Paths.get(path.toString(), fileName(tilePos)));
				return ByteBuffer.wrap(bytes).asLongBuffer().array();
			} catch (FileNotFoundException ignored) {
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new long[256];
		}

		private void writeTileTimestampsFile(ChunkPos tilePos, long[] chunkTimestamps) {
			try {
				final ByteBuffer buffer = ByteBuffer.allocate(8 * 256);
				buffer.asLongBuffer().put(chunkTimestamps);
				buffer.flip();
				Files.write(Paths.get(path.toString(), fileName(tilePos)), buffer.array());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

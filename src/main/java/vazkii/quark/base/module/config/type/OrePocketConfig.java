package vazkii.quark.base.module.config.type;

import java.util.Random;
import java.util.function.Consumer;

import net.minecraft.core.BlockPos;
import vazkii.quark.base.module.config.Config;

public class OrePocketConfig extends AbstractConfigType {

	@Config
	@Config.Min(-64)
	@Config.Max(320)
	private int minHeight;

	@Config
	@Config.Min(-64)
	@Config.Max(320)
	private int maxHeight;

	@Config
	@Config.Min(0)
	public int clusterSize;

	@Config(description = "Can be a positive integer or a fractional value betweeen 0 and 1. If integer, it spawns that many clusters. If fractional, it has that chance to spawn a single cluster. Set exactly zero to not spawn at all.")
	@Config.Min(0)
	public double clusterCount;

	public OrePocketConfig(int minHeight, int maxHeight, int clusterSize, double clusterCount) {
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		this.clusterSize = clusterSize;
		this.clusterCount = clusterCount;
	}

	public int getRandomHeight(Random rand) {
		return minHeight + rand.nextInt(maxHeight - minHeight);
	}

	public void forEach(BlockPos chunkCorner, Random rand, Consumer<BlockPos> callback) {
		if(clusterCount < 1 && clusterCount > 0)
			clusterCount = (rand.nextDouble() < clusterCount ? 1 : 0);
		
		for (int i = 0; i < clusterCount; i++) {
			int x = chunkCorner.getX() + rand.nextInt(16);
			int y = getRandomHeight(rand);
			int z = chunkCorner.getZ() + rand.nextInt(16);

			callback.accept(new BlockPos(x, y, z));
		}
	}

}

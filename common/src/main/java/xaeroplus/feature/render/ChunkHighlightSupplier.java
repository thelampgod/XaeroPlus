package xaeroplus.feature.render;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface ChunkHighlightSupplier {
    LongSet getHighlights(final int windowRegionX, final int windowRegionZ, final int windowRegionSize, ResourceKey<Level> dimension);
}
package net.minecraft.util.worldupdate;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;

@FunctionalInterface
public interface RegionStorageUpgrader$TagModifier {
   boolean modifyTagAfterFix(final ChunkPos pos, final CompoundTag upgradedTag);
}

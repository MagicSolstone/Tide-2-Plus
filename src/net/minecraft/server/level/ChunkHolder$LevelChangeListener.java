package net.minecraft.server.level;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import net.minecraft.world.level.ChunkPos;

@FunctionalInterface
public interface ChunkHolder$LevelChangeListener {
   void onLevelChange(ChunkPos pos, IntSupplier oldLevel, int newLevel, IntConsumer setQueueLevel);
}

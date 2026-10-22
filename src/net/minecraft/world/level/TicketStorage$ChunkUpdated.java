package net.minecraft.world.level;

@FunctionalInterface
public interface TicketStorage$ChunkUpdated {
   void update(final long node, final int newLevelFrom, final boolean onlyDecreased);
}

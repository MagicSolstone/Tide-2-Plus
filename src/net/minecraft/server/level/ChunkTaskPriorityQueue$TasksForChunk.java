package net.minecraft.server.level;

import java.util.List;

public record ChunkTaskPriorityQueue$TasksForChunk(long chunkPos, List<Runnable> tasks) {
}

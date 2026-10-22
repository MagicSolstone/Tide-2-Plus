package net.minecraft.world.level.chunk;

@FunctionalInterface
public interface LevelChunk$PostLoadProcessor {
   void run(LevelChunk levelChunk);
}

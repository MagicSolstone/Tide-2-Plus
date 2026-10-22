package net.minecraft.world.level.chunk;

import net.minecraft.util.ProblemReporter$PathElement;
import net.minecraft.world.level.ChunkPos;

record ChunkAccess$ChunkPathElement(ChunkPos pos) implements ProblemReporter$PathElement {
   @Override
   public String get() {
      return "chunk@" + this.pos;
   }
}

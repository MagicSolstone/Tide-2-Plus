package net.minecraft.world.level.chunk.status;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class ChunkPyramid$Builder {
   private final List<ChunkStep> steps = new ArrayList<>();

   public ChunkPyramid build() {
      return new ChunkPyramid(ImmutableList.copyOf(this.steps));
   }

   public ChunkPyramid$Builder step(final ChunkStatus status, final UnaryOperator<ChunkStep$Builder> operator) {
      ChunkStep$Builder stepBuilder;
      if (this.steps.isEmpty()) {
         stepBuilder = new ChunkStep$Builder(status);
      } else {
         stepBuilder = new ChunkStep$Builder(status, this.steps.getLast());
      }

      this.steps.add(operator.apply(stepBuilder).build());
      return this;
   }
}

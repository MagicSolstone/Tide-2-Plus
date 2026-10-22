package net.minecraft.world.level;

import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import org.jspecify.annotations.Nullable;

class ChunkPos$2 extends AbstractSpliterator<ChunkPos> {
   @Nullable
   private ChunkPos pos;

   ChunkPos$2(long est, int additionalCharacteristics, final ChunkPos val$from, final ChunkPos val$to, final int val$zDiff, final int val$xDiff) {
      super(est, additionalCharacteristics);
      this.val$from = val$fromx;
      this.val$to = val$tox;
      this.val$zDiff = val$zDiffx;
      this.val$xDiff = val$xDiffx;
   }

   @Override
   public boolean tryAdvance(final Consumer<? super ChunkPos> action) {
      if (this.pos == null) {
         this.pos = this.val$from;
      } else {
         int x = this.pos.x;
         int z = this.pos.z;
         if (x == this.val$to.x) {
            if (z == this.val$to.z) {
               return false;
            }

            this.pos = new ChunkPos(this.val$from.x, z + this.val$zDiff);
         } else {
            this.pos = new ChunkPos(x + this.val$xDiff, z);
         }
      }

      action.accept(this.pos);
      return true;
   }
}

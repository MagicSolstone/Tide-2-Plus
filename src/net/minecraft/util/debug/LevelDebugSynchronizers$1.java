package net.minecraft.util.debug;

import java.util.Objects;
import net.minecraft.world.level.chunk.LevelChunk;

class LevelDebugSynchronizers$1 implements DebugValueSource$Registration {
   LevelDebugSynchronizers$1(final LevelDebugSynchronizers this$0, final LevelChunk val$chunk) {
      this.val$chunk = val$chunkx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public <T> void register(final DebugSubscription<T> subscription, final DebugValueSource$ValueGetter<T> getter) {
      this.this$0.getSourceSynchronizer(subscription).registerChunk(this.val$chunk.getPos(), getter);
   }
}

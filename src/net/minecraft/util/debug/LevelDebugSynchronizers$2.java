package net.minecraft.util.debug;

import java.util.Objects;
import net.minecraft.world.level.block.entity.BlockEntity;

class LevelDebugSynchronizers$2 implements DebugValueSource$Registration {
   LevelDebugSynchronizers$2(final LevelDebugSynchronizers this$0, final BlockEntity val$blockEntity) {
      this.val$blockEntity = val$blockEntityx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public <T> void register(final DebugSubscription<T> subscription, final DebugValueSource$ValueGetter<T> getter) {
      this.this$0.getSourceSynchronizer(subscription).registerBlockEntity(this.val$blockEntity.getBlockPos(), getter);
   }
}

package net.minecraft.util.debug;

import java.util.Objects;
import net.minecraft.world.entity.Entity;

class LevelDebugSynchronizers$3 implements DebugValueSource$Registration {
   LevelDebugSynchronizers$3(final LevelDebugSynchronizers this$0, final Entity val$entity) {
      this.val$entity = val$entityx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public <T> void register(final DebugSubscription<T> subscription, final DebugValueSource$ValueGetter<T> getter) {
      this.this$0.getSourceSynchronizer(subscription).registerEntity(this.val$entity.getUUID(), getter);
   }
}

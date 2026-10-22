package net.minecraft.world.level.block.entity;

import java.util.Objects;
import java.util.Set;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import org.jspecify.annotations.Nullable;

class BlockEntity$1 implements DataComponentGetter {
   BlockEntity$1(final BlockEntity this$0, final Set val$implicitComponents, final DataComponentMap val$fullView) {
      this.val$implicitComponents = val$implicitComponentsx;
      this.val$fullView = val$fullViewx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Nullable
   @Override
   public <T> T get(final DataComponentType<? extends T> type) {
      this.val$implicitComponents.add(type);
      return this.val$fullView.get(type);
   }

   @Override
   public <T> T getOrDefault(final DataComponentType<? extends T> type, final T defaultValue) {
      this.val$implicitComponents.add(type);
      return this.val$fullView.getOrDefault(type, defaultValue);
   }
}

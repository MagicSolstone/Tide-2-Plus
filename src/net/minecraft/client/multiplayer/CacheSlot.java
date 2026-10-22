package net.minecraft.client.multiplayer;

import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class CacheSlot<C extends CacheSlot$Cleaner<C>, D> {
   private final Function<C, D> operation;
   @Nullable
   private C context;
   @Nullable
   private D value;

   public CacheSlot(final Function<C, D> operation) {
      this.operation = operation;
   }

   public D compute(final C context) {
      if (context == this.context && this.value != null) {
         return this.value;
      } else {
         D newValue = this.operation.apply(context);
         this.value = newValue;
         this.context = context;
         context.registerForCleaning(this);
         return newValue;
      }
   }

   public void clear() {
      this.value = null;
      this.context = null;
   }
}

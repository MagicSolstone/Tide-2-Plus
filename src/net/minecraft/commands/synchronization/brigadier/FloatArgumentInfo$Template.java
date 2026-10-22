package net.minecraft.commands.synchronization.brigadier;

import com.mojang.brigadier.arguments.FloatArgumentType;
import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo$Template;

public final class FloatArgumentInfo$Template implements ArgumentTypeInfo$Template<FloatArgumentType> {
   private final float min;
   private final float max;

   private FloatArgumentInfo$Template(final FloatArgumentInfo this$0, final float min, final float max) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.min = min;
      this.max = max;
   }

   public FloatArgumentType instantiate(final CommandBuildContext context) {
      return FloatArgumentType.floatArg(this.min, this.max);
   }

   @Override
   public ArgumentTypeInfo<FloatArgumentType, ?> type() {
      return this.this$0;
   }
}

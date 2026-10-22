package net.minecraft.commands.synchronization.brigadier;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo$Template;

public final class DoubleArgumentInfo$Template implements ArgumentTypeInfo$Template<DoubleArgumentType> {
   private final double min;
   private final double max;

   private DoubleArgumentInfo$Template(final DoubleArgumentInfo this$0, final double min, final double max) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.min = min;
      this.max = max;
   }

   public DoubleArgumentType instantiate(final CommandBuildContext context) {
      return DoubleArgumentType.doubleArg(this.min, this.max);
   }

   @Override
   public ArgumentTypeInfo<DoubleArgumentType, ?> type() {
      return this.this$0;
   }
}

package net.minecraft.commands.synchronization.brigadier;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo$Template;

public final class IntegerArgumentInfo$Template implements ArgumentTypeInfo$Template<IntegerArgumentType> {
   private final int min;
   private final int max;

   private IntegerArgumentInfo$Template(final IntegerArgumentInfo this$0, final int min, final int max) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.min = min;
      this.max = max;
   }

   public IntegerArgumentType instantiate(final CommandBuildContext context) {
      return IntegerArgumentType.integer(this.min, this.max);
   }

   @Override
   public ArgumentTypeInfo<IntegerArgumentType, ?> type() {
      return this.this$0;
   }
}

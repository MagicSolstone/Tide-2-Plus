package net.minecraft.commands.synchronization.brigadier;

import com.mojang.brigadier.arguments.LongArgumentType;
import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo$Template;

public final class LongArgumentInfo$Template implements ArgumentTypeInfo$Template<LongArgumentType> {
   private final long min;
   private final long max;

   private LongArgumentInfo$Template(final LongArgumentInfo this$0, final long min, final long max) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.min = min;
      this.max = max;
   }

   public LongArgumentType instantiate(final CommandBuildContext context) {
      return LongArgumentType.longArg(this.min, this.max);
   }

   @Override
   public ArgumentTypeInfo<LongArgumentType, ?> type() {
      return this.this$0;
   }
}

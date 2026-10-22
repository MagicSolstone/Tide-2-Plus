package net.minecraft.commands.arguments;

import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo$Template;

public final class TimeArgument$Info$Template implements ArgumentTypeInfo$Template<TimeArgument> {
   private final int min;

   private TimeArgument$Info$Template(final TimeArgument$Info this$0, final int min) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.min = min;
   }

   public TimeArgument instantiate(final CommandBuildContext context) {
      return TimeArgument.time(this.min);
   }

   @Override
   public ArgumentTypeInfo<TimeArgument, ?> type() {
      return this.this$0;
   }
}

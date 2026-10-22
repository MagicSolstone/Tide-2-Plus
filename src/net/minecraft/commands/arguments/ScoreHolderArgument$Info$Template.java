package net.minecraft.commands.arguments;

import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo$Template;

public final class ScoreHolderArgument$Info$Template implements ArgumentTypeInfo$Template<ScoreHolderArgument> {
   private final boolean multiple;

   private ScoreHolderArgument$Info$Template(final ScoreHolderArgument$Info this$0, final boolean multiple) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.multiple = multiple;
   }

   public ScoreHolderArgument instantiate(final CommandBuildContext context) {
      return new ScoreHolderArgument(this.multiple);
   }

   @Override
   public ArgumentTypeInfo<ScoreHolderArgument, ?> type() {
      return this.this$0;
   }
}

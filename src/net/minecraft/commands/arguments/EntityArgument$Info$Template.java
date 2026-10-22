package net.minecraft.commands.arguments;

import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo$Template;

public final class EntityArgument$Info$Template implements ArgumentTypeInfo$Template<EntityArgument> {
   private final boolean single;
   private final boolean playersOnly;

   private EntityArgument$Info$Template(final EntityArgument$Info this$0, final boolean single, final boolean playersOnly) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.single = single;
      this.playersOnly = playersOnly;
   }

   public EntityArgument instantiate(final CommandBuildContext context) {
      return new EntityArgument(this.single, this.playersOnly);
   }

   @Override
   public ArgumentTypeInfo<EntityArgument, ?> type() {
      return this.this$0;
   }
}

package net.minecraft.commands.synchronization;

import com.mojang.brigadier.arguments.ArgumentType;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.commands.CommandBuildContext;

public final class SingletonArgumentInfo$Template implements ArgumentTypeInfo$Template<A> {
   private final Function<CommandBuildContext, A> constructor;

   public SingletonArgumentInfo$Template(final Function<CommandBuildContext, A> this$0, final Function constructor) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.constructor = constructor;
   }

   @Override
   public A instantiate(final CommandBuildContext context) {
      return (A)((ArgumentType)this.constructor.apply(context));
   }

   @Override
   public ArgumentTypeInfo<A, ?> type() {
      return this.this$0;
   }
}

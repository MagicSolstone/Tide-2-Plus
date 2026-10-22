package net.minecraft.commands.synchronization.brigadier;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType.StringType;
import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo$Template;

public final class StringArgumentSerializer$Template implements ArgumentTypeInfo$Template<StringArgumentType> {
   private final StringType type;

   public StringArgumentSerializer$Template(final StringArgumentSerializer this$0, final StringType type) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.type = type;
   }

   public StringArgumentType instantiate(final CommandBuildContext context) {
      return switch (this.type) {
         case SINGLE_WORD -> StringArgumentType.word();
         case QUOTABLE_PHRASE -> StringArgumentType.string();
         case GREEDY_PHRASE -> StringArgumentType.greedyString();
         default -> throw new MatchException(null, null);
      };
   }

   @Override
   public ArgumentTypeInfo<StringArgumentType, ?> type() {
      return this.this$0;
   }
}

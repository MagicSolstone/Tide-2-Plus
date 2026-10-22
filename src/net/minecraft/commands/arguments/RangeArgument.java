package net.minecraft.commands.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.advancements.predicates.MinMaxBounds;

public interface RangeArgument<T extends MinMaxBounds<?>> extends ArgumentType<T> {
   static RangeArgument$Ints intRange() {
      return new RangeArgument$Ints();
   }

   static RangeArgument$Floats floatRange() {
      return new RangeArgument$Floats();
   }
}

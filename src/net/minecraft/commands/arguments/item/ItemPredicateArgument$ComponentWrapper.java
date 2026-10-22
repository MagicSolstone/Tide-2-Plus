package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Dynamic;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

record ItemPredicateArgument$ComponentWrapper(Identifier id, Predicate<ItemStack> presenceChecker, Decoder<? extends Predicate<ItemStack>> valueChecker) {
   public static <T> ItemPredicateArgument$ComponentWrapper create(final ImmutableStringReader reader, final Identifier id, final DataComponentType<T> type) throws CommandSyntaxException {
      Codec<T> codec = type.codec();
      if (codec == null) {
         throw ItemPredicateArgument.ERROR_UNKNOWN_COMPONENT.createWithContext(reader, id);
      } else {
         return new ItemPredicateArgument$ComponentWrapper(id, itemStack -> itemStack.has(type), codec.map(expected -> itemStack -> {
            T actual = itemStack.get(type);
            return Objects.equals(expected, actual);
         }));
      }
   }

   public Predicate<ItemStack> decode(final ImmutableStringReader reader, final Dynamic<?> value) throws CommandSyntaxException {
      DataResult<? extends Predicate<ItemStack>> result = this.valueChecker.parse(value);
      return (Predicate<ItemStack>)result.getOrThrow(
         message -> ItemPredicateArgument.ERROR_MALFORMED_COMPONENT.createWithContext(reader, this.id.toString(), message)
      );
   }
}
